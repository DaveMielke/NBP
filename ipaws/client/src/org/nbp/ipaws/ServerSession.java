package org.nbp.ipaws;

import android.util.Log;
import android.os.Build;

import android.content.Context;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.util.Map;
import java.util.HashMap;

public class ServerSession extends ApplicationComponent implements CommandReader, CommandWriter {
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

  @Override
  public final String readCommand () {
    try {
      String command = sessionReader.readLine();
      if (command == null) Log.w(LOG_TAG, "server disconnected");
      return command;
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket read error: " + exception.getMessage()));
    }

    return null;
  }

  private final Map<String, OperandsHandler> argumentsHandlers =
        new HashMap<String, OperandsHandler>()
  {
    {
      put("ping", new PingHandler(ServerSession.this));
      put("pong", new PongHandler());

      put("beginAlert", new BeginAlertHandler(ServerSession.this));
      put("removeAlert", new RemoveAlertHandler());

      put("allStates", new AllStatesHandler());
      put("stateCounties", new StateCountiesHandler());
    }
  };

  private final void doReceivedCommands () {
    OperandsHandler commandHandler =
      new OperandsHandler() {
        @Override
        public boolean handleOperands (String string) {
          String[] operands = getOperands(string, 2);
          int count = operands.length;
          int index = 0;

          String command = "";
          if (index < count) command = operands[index++];

          if (command.isEmpty()) return true;
          OperandsHandler argumentsHandler = argumentsHandlers.get(command);

          if (argumentsHandler == null) {
            Log.w(LOG_TAG, ("unrecognized command: " + command));
            return true;
          }

          String arguments = "";
          if (index < count) arguments = operands[index++];
          return argumentsHandler.handleOperands(arguments);
        }
      };

    while (true) {
      synchronized (this) {
        if (isStopping) break;
      }

      String command = readCommand();
      if (command == null) break;

      Log.d(LOG_TAG, ("received command: " + command));
      if (!commandHandler.handleOperands(command)) break;
    }
  }

  private final SocketAddress makeSocketAddress (String server) {
    return new InetSocketAddress(
      server,
      ApplicationParameters.SERVER_PORT
    );
  }

  private final void doSessionCommunication () {
    try {
      sessionSocket.setKeepAlive(true);

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
          doReceivedCommands();
        } finally {
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

            Log.d(LOG_TAG, "connected");
            AlertNotification.updateSessionState(R.string.session_stateConnected);

            doSessionCommunication();
            break;
          } finally {
            Log.d(LOG_TAG, "disconnecting");
            AlertNotification.updateSessionState(R.string.session_stateDisconnected);
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

    return connected;
  }

  private final void doSessionThread () {
    Log.d(LOG_TAG, "session thread startiong");
    AlertNotification.updateSessionState(R.string.session_stateDisconnected);

    try {
      int reconnectDelay = ApplicationParameters.RECONNECT_INITIAL_DELAY;

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
            wait(reconnectDelay * 1000L);
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
