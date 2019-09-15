package org.nbp.common.dictionary;

import android.util.Log;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;

public class DictionaryConnection {
  private final static String LOG_TAG = DictionaryConnection.class.getName();

  private DictionaryConnection () {
  }

  private final static Object SINGLETON_LOCK = new Object();
  private static DictionaryConnection dictionaryConnection = null;

  public static DictionaryConnection singleton () {
    synchronized (SINGLETON_LOCK) {
      if (dictionaryConnection == null) dictionaryConnection = new DictionaryConnection();
      return dictionaryConnection;
    }
  }

  private static SocketAddress makeServerAddress () {
    return new InetSocketAddress(
      DictionaryParameters.SERVER_NAME,
      DictionaryParameters.SERVER_PORT
    );
  }

  private static void close (Closeable closeable, String what) {
    try {
      closeable.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG,
        String.format(
          "%s close error: %s",
          what, exception.getMessage()
        )
      );
    }
  }

  private Socket clientSocket = null;
  private BufferedReader responseReader = null;
  private Writer commandWriter = null;
  private Thread responseThread = null;

  private final void closeSocket () {
    if (clientSocket != null) {
      close(clientSocket, "socket");
      clientSocket = null;
    }
  }

  private final void closeReader () {
    if (responseReader != null) {
      close(responseReader, "reader");
      responseReader = null;
    }
  }

  private final void closeWriter () {
    if (commandWriter != null) {
      close(commandWriter, "writer");
      commandWriter = null;
    }
  }

  private final void closeConnection () {
    closeReader();
    closeWriter();
    closeSocket();
  }

  private final Socket getSocket () {
    if (clientSocket == null) {
      Socket socket = new Socket();

      try {
        Log.d(LOG_TAG, "connecting");
        socket.connect(makeServerAddress());
        Log.d(LOG_TAG, "connected");
        clientSocket = socket;
      } catch (IOException exception) {
        Log.e(LOG_TAG, ("connect error: " + exception.getMessage()));
      }
    }

    return clientSocket;
  }

  private final BufferedReader getReader () {
    if (responseReader == null) {
      Socket socket = getSocket();

      if (socket != null) {
        try {
          InputStream stream = socket.getInputStream();
          Reader reader = new InputStreamReader(stream, DictionaryParameters.CHARACTER_ENCODING);
          return (responseReader = new BufferedReader(reader));
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("reader creation error: " + exception.getMessage()));
        }
      }
    }

    return null;
  }

  private final Writer getWriter () {
    if (commandWriter == null) {
      Socket socket = getSocket();

      if (socket != null) {
        try {
          OutputStream stream = socket.getOutputStream();
          return (commandWriter = new OutputStreamWriter(stream, DictionaryParameters.CHARACTER_ENCODING));
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("writer creation error: " + exception.getMessage()));
        }
      }
    }

    return null;
  }

  private final void startResponseThread () {
    if (responseThread == null) {
      responseThread = new Thread() {
        @Override
        public void run () {
          Log.d(LOG_TAG, "response thread starting");

          try {
            BufferedReader reader = getReader();

            if (reader != null) {
              while (true) {
                String line;

                try {
                  line = reader.readLine();
                } catch (IOException exception) {
                  Log.e(LOG_TAG, ("input error: " + exception.getMessage()));
                  break;
                }

                if (line == null) {
                  Log.d(LOG_TAG, "end of input");
                  break;
                }

                Log.i(LOG_TAG, ("response: " + line));
              }
            }
          } finally {
            Log.d(LOG_TAG, "response thread finished");

            synchronized (DictionaryConnection.this) {
              closeConnection();
            }
          }
        }
      };

      responseThread.start();
    }
  }

  public final boolean writeCommand (String... operands) {
    StringBuilder command = new StringBuilder();

    for (String operand : operands) {
      if (command.length() > 0) command.append(' ');
      command.append(operand);
    }

    if (command.length() == 0) return true;
    Log.d(LOG_TAG, ("command: " + command));
    command.append("\r\n");

    synchronized (this) {
      Writer writer = getWriter();

      if (writer != null) {
        try {
          writer.write(command.toString());
          writer.flush();
          startResponseThread();
          return true;
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("write error: " + exception.getMessage()));
        }
      }

      closeConnection();
    }

    return false;
  }
}
