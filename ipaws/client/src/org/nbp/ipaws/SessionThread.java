package org.nbp.ipaws;

import android.util.Log;

import android.content.Context;
import android.content.res.Resources;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class SessionThread extends Thread {
  private final static String LOG_TAG = SessionThread.class.getName();

  private final static int INITIAL_DELAY =   1000;
  private final static int MAXIMUM_DELAY = 300000;
  private final static String DATA_ENCODING = "UTF8";

  private Context sessionContext = null;
  private BufferedWriter sessionWriter = null;
  private BufferedReader sessionReader = null;

  public SessionThread (Context context) {
    super("session-thread");
    sessionContext = context;
  }

  public final void sendCommand (String command) throws IOException {
    synchronized (sessionWriter) {
      Log.d(LOG_TAG, ("sending command: " + command));
      sessionWriter.write(command);
      sessionWriter.write('\n');
      sessionWriter.flush();
    }
  }

  public final void sendCommand (StringBuilder command) throws IOException {
    sendCommand(command.toString());
  }

  public final void setAreas () throws IOException {
    StringBuilder command = new StringBuilder("setAreas");
    command.append(" 000000");
    sendCommand(command);
  }

  private final void haveAlerts () throws IOException {
    StringBuilder command = new StringBuilder("haveAlerts");
    sendCommand(command);
  }

  private final void handleResponse (String response) {
  }

  private final void handleResponses () throws IOException {
    String response;

    while ((response = sessionReader.readLine()) != null) {
      Log.d(LOG_TAG, ("received response: " + response));
      handleResponse(response);
    }
  }

  private final SocketAddress makeSocketAddress () {
    Resources resources = sessionContext.getResources();

    return new InetSocketAddress(
      resources.getString(R.string.server_name),
      resources.getInteger(R.integer.server_port)
    );
  }

  private final boolean doSession () {
    boolean connected = false;

    try {
      SocketAddress address = makeSocketAddress();
      Log.d(LOG_TAG, ("connecting to " + address.toString()));

      Socket socket = new Socket();
      socket.connect(address);

      try {
        connected = true;
        Log.d(LOG_TAG, "connected");

        try {
          socket.setKeepAlive(true);

          sessionWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
          try {
            setAreas();
            haveAlerts();
            sendCommand("sendAlerts");

            sessionReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        Log.d(LOG_TAG, "disconnecting");
        socket.close();
        socket = null;
      }
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket error: " + exception.getMessage()));
    }

    return connected;
  }

  @Override
  public void run () {
    Log.d(LOG_TAG, "startiong");

    try {
      int currentDelay = INITIAL_DELAY;

      while (true) {
        currentDelay = doSession()? INITIAL_DELAY: (currentDelay << 1);
        if (currentDelay > MAXIMUM_DELAY) currentDelay = MAXIMUM_DELAY;

        try {
          Log.d(LOG_TAG, "waiting");
          sleep(currentDelay);
        } catch (InterruptedException exception) {
          Log.d(LOG_TAG, "interrupted");
          break;
        }
      }
    } finally {
      Log.d(LOG_TAG, "stoppiong");
    }
  }
}
