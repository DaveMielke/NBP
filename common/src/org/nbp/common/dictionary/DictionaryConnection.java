package org.nbp.common.dictionary;

import android.util.Log;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import java.io.Closeable;
import java.io.IOException;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;

import java.util.concurrent.LinkedBlockingQueue;

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
  private Writer commandWriter = null;
  private BufferedReader responseReader = null;

  private Thread commandThread = null;
  private Thread responseThread = null;

  private class CommandEntry {
    public final ResponseHandler handler;
    public final String[] arguments;

    public CommandEntry (ResponseHandler handler, String... arguments) {
      this.handler = handler;
      this.arguments = arguments;
    }
  }

  private final LinkedBlockingQueue<CommandEntry> commandQueue =
            new LinkedBlockingQueue<CommandEntry>();

  private final LinkedBlockingQueue<ResponseHandler> responseQueue =
            new LinkedBlockingQueue<ResponseHandler>();

  private final void closeSocket () {
    if (clientSocket != null) {
      try {
        clientSocket.close();
      } catch (IOException exception) {
        Log.w(LOG_TAG, ("socket close error: " + exception.getMessage()));
      }

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
    synchronized (this) {
      closeReader();
      closeWriter();
      closeSocket();
    }
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
      responseThread = new Thread("dictionary-response-thread") {
        private final int parseResponseCode (String operand) {
          int value;

          try {
            value = Integer.parseUnsignedInt(operand, 10);

            if (!Character.isDigit(operand.charAt(0))) {
              throw new NumberFormatException();
            }
          } catch (NumberFormatException exception) {
            throw new ResponseException(operand, "response code is not an integer");
          }

          if (operand.length() != 3) {
            throw new ResponseException(operand, "response code is not three digits");
          }

          return value;
        }

        @Override
        public void run () {
          Log.d(LOG_TAG, "response thread starting");

          try {
            BufferedReader reader = getReader();

            if (reader != null) {
              while (true) {
                String response;

                try {
                  response = reader.readLine();
                } catch (IOException exception) {
                  Log.e(LOG_TAG, ("input error: " + exception.getMessage()));
                  break;
                }

                if (response == null) {
                  Log.d(LOG_TAG, "end of input");
                  break;
                }

                Log.i(LOG_TAG, ("response: " + response));
                DictionaryOperands operands;

                try {
                  operands = new DictionaryOperands(response);

                  if (operands.isEmpty()) {
                    Log.w(LOG_TAG, "missing response code");
                    continue;
                  }

                  int code = parseResponseCode(operands.removeFirst());
                } catch (ResponseException exception) {
                  Log.w(LOG_TAG,
                    String.format(
                      "illegal response operand: %s: %s",
                      exception.getProblem(), exception.getMessage()
                    )
                  );

                  continue;
                }
              }
            }
          } finally {
            Log.d(LOG_TAG, "response thread finished");
            closeConnection();
          }
        }
      };

      responseThread.start();
    }
  }

  private final void startCommandThread () {
    if (commandThread == null) {
      commandThread = new Thread("dictionary-command-thread") {
        @Override
        public void run () {
          Log.d(LOG_TAG, "command thread starting");

          try {
            StringBuilder command = new StringBuilder();

          COMMAND_LOOP:
            while (true) {
              CommandEntry entry;

              try {
                entry = commandQueue.take();
              } catch (InterruptedException exception) {
                Log.w(LOG_TAG, "command thread interrupted");
                break;
              }

              String[] arguments = entry.arguments;
              if (arguments.length == 0) continue;
              command.setLength(0);

              for (String argument : arguments) {
                if (command.length() > 0) command.append(' ');
                String operand;

                try {
                  operand = DictionaryOperands.quote(argument);
                } catch (CommandException exception) {
                  Log.w(LOG_TAG,
                    String.format(
                      "illegal command operand: %s: %s",
                      exception.getProblem(), exception.getMessage()
                    )
                  );

                  continue COMMAND_LOOP;
                }

                command.append(operand);
              }

              if (command.length() == 0) continue;
              Log.d(LOG_TAG, ("command: " + command));
              command.append("\r\n");

              synchronized (DictionaryConnection.this) {
                Writer writer = getWriter();
                if (writer == null) break;

                try {
                  writer.write(command.toString());
                  writer.flush();
                } catch (IOException exception) {
                  Log.e(LOG_TAG, ("write error: " + exception.getMessage()));
                  break;
                }
              }

              responseQueue.offer(entry.handler);
              startResponseThread();
            }
          } finally {
            Log.d(LOG_TAG, "command thread finished");
            closeConnection();
          }
        }
      };

      commandThread.start();
    }
  }

  public final void startCommand (ResponseHandler handler, String... arguments) {
    startCommandThread();
    commandQueue.offer(new CommandEntry(handler, arguments));
  }
}
