package org.nbp.ipaws;

import android.util.Log;

import android.content.Context;
import android.os.Build;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.net.SocketException;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.nbp.common.Timeout;

public class ServerSession extends ApplicationComponent implements SessionOperations {
  private final static String LOG_TAG = ServerSession.class.getName();

  private final Thread sessionThread;
  private boolean isStopping = false;

  private Socket sessionSocket = null;
  private Writer sessionWriter = null;
  private BufferedReader sessionReader = null;

  private final void closeSocket () {
    if (!sessionSocket.isClosed()) {
      try {
        sessionSocket.close();
      } catch (IOException exception) {
        Log.w(LOG_TAG, ("socket close error: " + exception.getMessage()));
      }
    }
  }

  @Override
  public final boolean setReadTimeout (long milliseconds) {
    final long minimum = 1;
    final long maximum = Integer.MAX_VALUE;

    if (milliseconds >= minimum) {
      if (milliseconds <= maximum) {
        Log.d(LOG_TAG, ("setting read timeout: " + milliseconds));

        try {
          sessionSocket.setSoTimeout((int)milliseconds);
          return true;
        } catch (SocketException exception) {
          Log.w(LOG_TAG, ("socket set timeout exception: " + exception.getMessage()));
        }
      } else {
        Log.w(LOG_TAG,
          String.format(
            "socket timeout too long: %d > %d",
            milliseconds, maximum
          )
        );
      }
    } else {
      Log.w(LOG_TAG,
        String.format(
          "socket timeout too short: %d < %d",
          milliseconds, minimum
        )
      );
    }

    return false;
  }

  public final boolean writeCommand (String command) {
    Writer writer;

    synchronized (this) {
      writer = sessionWriter;
    }

    if (writer != null) {
      synchronized (writer) {
        try {
          Log.d(LOG_TAG, ("sending command: " + command));

          writer.write(command);
          writer.write('\n');
          writer.flush();

          return true;
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("socket write error: " + exception.getMessage()));
          closeSocket();
        }
      }
    } else {
      Log.w(LOG_TAG, "not connected to server");
    }

    return false;
  }

  @Override
  public final boolean writeCommand (StringBuilder command) {
    return writeCommand(command.toString());
  }

  public final boolean getStates () {
    return writeCommand("getStates");
  }

  public final boolean getCounties (Areas.State state) {
    StringBuilder command = new StringBuilder("getCounties");
    command.append(' ');
    command.append(state.getAbbreviation());
    return writeCommand(command);
  }

  private final boolean sendIdentity () {
    StringBuilder command = new StringBuilder("identity");

    command.append(' ');
    command.append(Build.SERIAL);

    command.append(' ');
    command.append(Build.VERSION.SDK_INT);

    command.append(' ');
    command.append(Build.MODEL);

    return writeCommand(command);
  }

  private final boolean haveAlerts () {
    StringBuilder command = new StringBuilder("haveAlerts");

    for (String identifier : Alerts.list(false)) {
      command.append(' ');
      command.append(identifier);
    }

    return writeCommand(command);
  }

  public final boolean  setAreas () {
    StringBuilder command = new StringBuilder("setAreas");

    for (String area : getRequestedAreas()) {
      command.append(' ');
      command.append(area);
    }

  //command.append(" 000000");
    return writeCommand(command);
  }

  private static int pingNumber = 0;
  private final Timeout pingSender =
    new Timeout(ApplicationParameters.PING_SEND_INTERVAL, "ping-sender") {
      @Override
      public void run () {
        start();

        {
          StringBuilder command = new StringBuilder("ping");

          command.append(' ');
          command.append(++pingNumber);

          command.append(' ');
          command.append(TimeUnit.MILLISECONDS.toSeconds(getDelay()));

          writeCommand(command.toString());
        }
      }
    };

  @Override
  public final String readLine () {
    try {
      String line = sessionReader.readLine();
      if (line == null) Log.w(LOG_TAG, "server disconnected");
      return line;
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket read error: " + exception.getMessage()));
    }

    return null;
  }

  private final Map<String, CommandHandler> commandHandlers =
        new HashMap<String, CommandHandler>()
  {
    {
      SessionOperations operations = ServerSession.this;

      put("deny", new DenyHandler(operations));
      put("ping", new PingHandler(operations));
      put("pong", new PongHandler(operations));

      put("beginAlert", new BeginAlertHandler(operations));
      put("removeAlert", new RemoveAlertHandler(operations));

      put("allStates", new AllStatesHandler(operations));
      put("stateCounties", new StateCountiesHandler(operations));
    }
  };

  private final void doReceivedCommands () {
    OperandsHandler lineHandler =
      new OperandsHandler() {
        @Override
        public boolean handleOperands (String string) {
          String[] operands = getOperands(string, 2);
          int count = operands.length;
          int index = 0;

          String command = "";
          if (index < count) command = operands[index++];

          if (command.isEmpty()) return true;
          CommandHandler commandHandler = commandHandlers.get(command);

          if (commandHandler == null) {
            Log.w(LOG_TAG, ("unrecognized command: " + command));
            return true;
          }

          String arguments = "";
          if (index < count) arguments = operands[index++];
          return commandHandler.handleOperands(arguments);
        }
      };

    while (true) {
      synchronized (this) {
        if (isStopping) break;
      }

      String line = readLine();
      if (line == null) break;

      Log.d(LOG_TAG, ("received command: " + line));
      if (!lineHandler.handleOperands(line)) break;
    }
  }

  private final SocketAddress makeSocketAddress (String server) {
    return new InetSocketAddress(
      server, ApplicationParameters.SERVER_PORT
    );
  }

  private final void doSessionCommunication () {
    try {
      sessionSocket.setKeepAlive(true);
      setReadTimeout(ApplicationParameters.PING_RECEIVE_TIMEOUT);

      synchronized (this) {
        sessionWriter =
          new BufferedWriter(
            new OutputStreamWriter(
              sessionSocket.getOutputStream(),
              ApplicationParameters.CHARACTER_ENCODING
            )
          );
      }

      try {
        synchronized (this) {
          sessionReader =
            new BufferedReader(
              new InputStreamReader(
                sessionSocket.getInputStream(),
                ApplicationParameters.CHARACTER_ENCODING
              )
            );
        }

        try {
          if (!sendIdentity()) return;
          if (!haveAlerts()) return;
          if (!setAreas()) return;
          if (!writeCommand("sendAlerts")) return;

          pingSender.start();
          doReceivedCommands();
        } finally {
          pingSender.cancel();

          synchronized (this) {
            sessionReader = null;
          }
        }
      } finally {
        synchronized (this) {
          sessionWriter = null;
        }
      }
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket comunication error: " + exception.getMessage()));
    }
  }

  private final boolean doSessionConnection () {
    AlertNotification.updateSessionState(R.string.session_stateConnecting);
    boolean connected = false;

    String[] servers = new String[] {
      ApplicationSettings.PRIMARY_SERVER,
      ApplicationSettings.SECONDARY_SERVER
    };

    for (String server : servers) {
      if (server == null) continue;
      if (server.isEmpty()) continue;

      SocketAddress address = makeSocketAddress(server);
      Log.d(LOG_TAG, ("connecting to " + address.toString()));

      synchronized (this) {
        sessionSocket = new Socket();
      }

      try {
        try {
          sessionSocket.connect(address);

          try {
            connected = true;
            AlertNotification.updateSessionState(R.string.session_stateConnected);

            {
              SocketAddress local = sessionSocket.getLocalSocketAddress();
              SocketAddress remote = sessionSocket.getRemoteSocketAddress();

              Log.d(LOG_TAG,
                String.format(
                  "connected: %s -> %s",
                  local.toString(), remote.toString()
                )
              );
            }

            doSessionCommunication();
            break;
          } finally {
            Log.d(LOG_TAG, "disconnecting");
          }
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("socket connection error: " + exception.getMessage()));
        }
      } finally {
        synchronized (this) {
          closeSocket();
          sessionSocket = null;
        }
      }
    }

    AlertNotification.updateSessionState(R.string.session_stateDisconnected);
    return connected;
  }

  private final void doSessionThread () {
    Log.d(LOG_TAG, "session thread startiong");
    AlertNotification.updateSessionState(R.string.session_stateDisconnected);

    try {
      long reconnectDelay = ApplicationParameters.RECONNECT_INITIAL_DELAY;

      while (!isStopping) {
        reconnectDelay = doSessionConnection()?
                         ApplicationParameters.RECONNECT_INITIAL_DELAY:
                         (reconnectDelay << 1);

        if (reconnectDelay > ApplicationParameters.RECONNECT_MAXIMUM_DELAY) {
          reconnectDelay = ApplicationParameters.RECONNECT_MAXIMUM_DELAY;
        }

        synchronized (this) {
          if (isStopping) break;

          try {
            Log.d(LOG_TAG, "delaying before reconnect");
            wait(reconnectDelay);
          } catch (InterruptedException exception) {
            Log.w(LOG_TAG, ("reconnect delay interrupted: " + exception.getMessage()));
          }
        }
      }
    } finally {
      AlertNotification.updateSessionState(R.string.session_stateOff);
      Log.d(LOG_TAG, "session thread done");
    }
  }

  public final void endSession () {
    synchronized (this) {
      if (!isStopping) {
        Log.d(LOG_TAG, "ending session");
        isStopping = true;

        new ServerAction() {
          @Override
          public boolean perform (ServerSession session) {
            synchronized (session) {
              try {
                return session.writeCommand("end");
              } finally {
                session.notify();
              }
            }
          }
        }.perform();

        try {
          wait(ApplicationParameters.RESPONSE_TIMEOUT);
        } catch (InterruptedException exception) {
          Log.w(LOG_TAG, ("end wait interrupted: " + exception.getMessage()));
        }
      }

      if (sessionSocket != null) closeSocket();
    }
  }

  public ServerSession () {
    super();

    sessionThread = new Thread(
      new Runnable() {
        @Override
        public void run () {
          doSessionThread();
        }
      }
    );

    sessionThread.setName("session-thread");
    sessionThread.start();
  }
}
