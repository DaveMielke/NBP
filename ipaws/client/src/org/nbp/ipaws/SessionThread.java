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

  private Context sessionContext = null;
  private BufferedWriter sessionWriter = null;
  private BufferedReader sessionReader = null;

  public SessionThread (Context context) {
    super("session-thread");
    sessionContext = context;
  }

  private final void handleResponse (String response) {
  }

  public final void sendCommand (StringBuilder command) throws IOException {
    synchronized (sessionWriter) {
      sessionWriter.write(command.toString());
      sessionWriter.write('\n');
      sessionWriter.flush();
    }
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

  private final void sendAlerts () throws IOException {
    StringBuilder command = new StringBuilder("sendAlerts");
    sendCommand(command);
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
      Socket socket = new Socket();

      socket.connect(makeSocketAddress());
      try {
        connected = true;
        socket.setKeepAlive(true);

        sessionWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
          setAreas();
          haveAlerts();
          sendAlerts();

          sessionReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          try {
            String response;

            while ((response = sessionReader.readLine()) != null) {
              handleResponse(response);
            }
          } finally {
            sessionReader = null;
          }
        } finally {
          sessionWriter = null;
        }
      } finally {
        try {
          socket.close();
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("socket close error: " + exception.getMessage()));
        }

        socket = null;
      }
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket I/O error: " + exception.getMessage()));
    }

    return connected;
  }

  @Override
  public void run () {
    int currentDelay = INITIAL_DELAY;

    while (true) {
      currentDelay = doSession()? INITIAL_DELAY: (currentDelay << 1);
      if (currentDelay > MAXIMUM_DELAY) currentDelay = MAXIMUM_DELAY;

      try {
        sleep(currentDelay);
      } catch (InterruptedException exception) {
      }
    }
  }
}
