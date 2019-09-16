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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DictionaryConnection implements Closeable {
  private final static String LOG_TAG = DictionaryConnection.class.getName();

  private DictionaryConnection () {
  }

  private final static Object GET_LOCK = new Object();
  private static DictionaryConnection currentConnection = null;

  public static DictionaryConnection get () {
    synchronized (GET_LOCK) {
      if (currentConnection == null) currentConnection = new DictionaryConnection();
      return currentConnection;
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

  private final BlockingQueue<CommandEntry> commandQueue =
      new LinkedBlockingQueue<CommandEntry>();

  private final BlockingQueue<ResponseHandler> responseQueue =
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

  @Override
  public void close () {
    synchronized (GET_LOCK) {
      if (this == currentConnection) currentConnection = null;
    }

    synchronized (this) {
      closeReader();
      closeWriter();
      closeSocket();

      while (true) {
        ResponseHandler handler;

        synchronized (DictionaryConnection.this) {
          handler = responseQueue.poll();
        }

        if (handler == null) break;
        handler.setFinished();
      }
    }
  }

  private final Socket getSocket () {
    if (clientSocket == null) {
      Socket socket = new Socket();

      try {
        Log.d(LOG_TAG, "client connecting");
        socket.connect(makeServerAddress());
        Log.d(LOG_TAG, "client connected");
        clientSocket = socket;
      } catch (IOException exception) {
        Log.e(LOG_TAG, ("client connect error: " + exception.getMessage()));
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
          responseReader = new BufferedReader(reader);
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("reader creation error: " + exception.getMessage()));
        }
      }
    }

    return responseReader;
  }

  private final Writer getWriter () {
    if (commandWriter == null) {
      Socket socket = getSocket();

      if (socket != null) {
        try {
          OutputStream stream = socket.getOutputStream();
          commandWriter = new OutputStreamWriter(stream, DictionaryParameters.CHARACTER_ENCODING);
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("writer creation error: " + exception.getMessage()));
        }
      }
    }

    return commandWriter;
  }

  public final String readLine () {
    BufferedReader reader = getReader();
    if (reader == null) return null;

    try {
      String line = reader.readLine();
      if (line != null) return line;
      Log.d(LOG_TAG, "end of input");
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("socket read error: " + exception.getMessage()));
    }

    return null;
  }

  private final void handleBanner (DictionaryOperands operands) {
  }

  private final boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.SERVER_BANNER:
        handleBanner(operands);
        return true;

      default:
        return false;
    }
  }

  private final void startResponseThread () {
    if (responseThread == null) {
      responseThread = new Thread("dictionary-response-thread") {
        private final int parseResponseCode (String operand) {
          int value;

          try {
            value = Integer.parseInt(operand, 10);

            if (!Character.isDigit(operand.charAt(0))) {
              throw new NumberFormatException();
            }
          } catch (NumberFormatException exception) {
            throw new OperandException(
              "response code is not an integer", operand
            );
          }

          if (operand.length() != 3) {
            throw new OperandException(
              "response code is not three digits", operand
            );
          }

          return value;
        }

        @Override
        public void run () {
          Log.d(LOG_TAG, "response thread starting");

          try {
            while (true) {
              String response = readLine();
              if (response == null) break;
              Log.i(LOG_TAG, ("response: " + response));

              try {
                DictionaryOperands operands = new DictionaryOperands(response);

                if (operands.isEmpty()) {
                  throw new OperandException("missing response code");
                }

                int code = parseResponseCode(operands.removeFirst());
                if (handleResponse(code, operands)) continue;
                ResponseHandler handler = responseQueue.peek();

                if (handler == null) {
                  throw new OperandException("no response handler");
                }

                if (handler.handleResponse(code, operands)) {
                  responseQueue.remove();
                  handler.setFinished();
                }
              } catch (OperandException exception) {
                Log.w(LOG_TAG, exception.getMessage());
              }
            }
          } finally {
            Log.d(LOG_TAG, "response thread finished");
            close();
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

              try {
                for (String argument : arguments) {
                  if (command.length() > 0) command.append(' ');
                  command.append(DictionaryOperands.quote(argument));
                }

                if (command.length() == 0) continue;
                Log.d(LOG_TAG, ("command: " + command));
                command.append("\r\n");

                {
                  int maximum = DictionaryParameters.MAXIMUM_LENGTH;
                  int length = command.length();

                  if (length > maximum) {
                    throw new OperandException(
                      String.format(
                        "command line too long: %d > %d",
                        length, maximum
                      )
                    );
                  }
                }

                synchronized (DictionaryConnection.this) {
                  Writer writer = getWriter();
                  if (writer == null) break;

                  try {
                    writer.write(command.toString());
                    writer.flush();
                  } catch (IOException exception) {
                    Log.e(LOG_TAG, ("socket write error: " + exception.getMessage()));
                    break;
                  }

                  responseQueue.offer(entry.handler);
                  startResponseThread();
                }
              } catch (OperandException exception) {
                Log.w(LOG_TAG, exception.getMessage());
              }
            }
          } finally {
            Log.d(LOG_TAG, "command thread finished");
            close();
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