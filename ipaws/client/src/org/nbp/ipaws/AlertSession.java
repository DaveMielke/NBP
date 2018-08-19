package org.nbp.ipaws;

import android.util.Log;

import android.content.Context;
import android.content.res.Resources;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class AlertSession extends AlertComponent {
  private final static String LOG_TAG = AlertSession.class.getName();

  private final Thread sessionThread;
  private boolean isStopping = false;

  private Socket sessionSocket = null;
  private Writer sessionWriter = null;
  private BufferedReader sessionReader = null;

  public final void writeCommand (String command) throws IOException {
    synchronized (sessionWriter) {
      Log.d(LOG_TAG, ("sending command: " + command));
      sessionWriter.write(command);
      sessionWriter.write('\n');
      sessionWriter.flush();
    }
  }

  public final void writeCommand (StringBuilder command) throws IOException {
    writeCommand(command.toString());
  }

  public final void setAreas () throws IOException {
    StringBuilder command = new StringBuilder("setAreas");
    command.append(" 000000");
    writeCommand(command);
  }

  private final void haveAlerts () throws IOException {
    StringBuilder command = new StringBuilder("haveAlerts");
    writeCommand(command);
  }

  private final String readResponse () {
    try {
      String response = sessionReader.readLine();
      if (!Thread.interrupted()) return response;
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("session read error: " + exception.getMessage()));
    }

    return null;
  }

  private final void handleResponse (String response) {
  }

  private final void handleResponses () {
    while (true) {
      String response = readResponse();
      if (response == null) break;

      Log.d(LOG_TAG, ("received response: " + response));
      handleResponse(response);
    }
  }

  private final SocketAddress makeSocketAddress () {
    return new InetSocketAddress(
      ApplicationParameters.SERVER_NAME,
      ApplicationParameters.SERVER_PORT
    );
  }

  private final boolean doSession () {
    boolean connected = false;

    synchronized (this) {
      sessionSocket = new Socket();
    }

    try {
      SocketAddress address = makeSocketAddress();
      Log.d(LOG_TAG, ("connecting to " + address.toString()));
      sessionSocket.connect(address);

      try {
        connected = true;
        Log.d(LOG_TAG, "connected");

        try {
          sessionSocket.setKeepAlive(true);

          sessionWriter =
            new BufferedWriter(
              new OutputStreamWriter(
                sessionSocket.getOutputStream(),
                ApplicationParameters.CHARACTER_ENCODING
              )
            );

          try {
            setAreas();
            haveAlerts();
            writeCommand("sendAlerts");

            sessionReader =
              new BufferedReader(
                new InputStreamReader(
                  sessionSocket.getInputStream(),
                  ApplicationParameters.CHARACTER_ENCODING
                )
              );

            try {
              handleResponses();
            } finally {
              sessionReader = null;
            }
          } finally {
            sessionWriter = null;
          }
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("session error: " + exception.getMessage()));
        }
      } finally {
        synchronized (this) {
          Log.d(LOG_TAG, "disconnecting");
          sessionSocket.close();
          sessionSocket = null;
        }
      }
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket error: " + exception.getMessage()));
    }

    return connected;
  }

  public final void doThread () {
    Log.d(LOG_TAG, "startiong");

    try {
      int currentDelay = ApplicationParameters.RECONNECT_INITIAL_DELAY;

      while (true) {
        currentDelay =
          doSession()?
          ApplicationParameters.RECONNECT_INITIAL_DELAY:
          (currentDelay << 1);

        if (currentDelay > ApplicationParameters.RECONNECT_MAXIMUM_DELAY) {
          currentDelay = ApplicationParameters.RECONNECT_MAXIMUM_DELAY;
        }

        synchronized (this) {
          if (isStopping) break;

          try {
            Log.d(LOG_TAG, "waiting");
            wait(currentDelay * 1000);
          } catch (InterruptedException exception) {
            Log.d(LOG_TAG, "interrupted");
            break;
          }
        }
      }
    } finally {
      Log.d(LOG_TAG, "stopped");
    }
  }

  public final void stop () {
    synchronized (this) {
      if (!isStopping) {
        Log.d(LOG_TAG, "stopping");
        isStopping = true;
        sessionThread.interrupt();
      }

      if (sessionSocket != null) {
        try {
          sessionSocket.close();
        } catch (IOException exception) {
          Log.w(LOG_TAG, ("socket close error: " + exception.getMessage()));
        }
      }
    }
  }

  public AlertSession (Context context) {
    super(context);

    sessionThread = new Thread(
      new Runnable() {
        @Override
        public void run () {
          doThread();
        }
      }
    );

    sessionThread.setName("session-thread");
    sessionThread.start();
  }
}
