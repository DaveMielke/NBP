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

  private final static Object IDENTIFIER_LOCK = new Object();
  private static int previousIdentifier = 0;
  private final int currentIdentifier;

  private DictionaryConnection () {
    synchronized (IDENTIFIER_LOCK) {
      currentIdentifier = ++previousIdentifier;
    }
  }

  public final int getIdentifier () {
    return currentIdentifier;
  }

  private final void logConnectionState (String state) {
    Log.d(LOG_TAG,
      String.format(
        "client %s: %d",
        state, getIdentifier()
      )
    );
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

  private Socket clientSocket = null;
  private Writer commandWriter = null;
  private BufferedReader responseReader = null;

  private Thread requestThread = null;
  private Thread responseThread = null;

  private class RequestEntry {
    public final RequestHandler handler;
    public final String[] arguments;

    public RequestEntry (RequestHandler handler, String... arguments) {
      this.handler = handler;
      this.arguments = arguments;
    }
  }

  private final BlockingQueue<RequestEntry> commandQueue =
      new LinkedBlockingQueue<RequestEntry>();

  private final BlockingQueue<RequestHandler> responseQueue =
      new LinkedBlockingQueue<RequestHandler>();

  private final void flushResponseQueue () {
    while (true) {
      RequestHandler handler = responseQueue.poll();
      if (handler == null) break;
      handler.setFinished();
    }
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

  private final void closeSocket () {
    if (clientSocket != null) {
      if (!clientSocket.isClosed()) {
        logConnectionState("disconnecting");

        try {
          closeReader();
          closeWriter();

          try {
            clientSocket.close();
          } catch (IOException exception) {
            Log.w(LOG_TAG, ("socket close error: " + exception.getMessage()));
          }
        } finally {
          logConnectionState("disconnected");
        }
      }
    }
  }

  @Override
  public void close () {
    synchronized (GET_LOCK) {
      if (this == currentConnection) currentConnection = null;
    }

    synchronized (this) {
      closeSocket();
      flushResponseQueue();
    }
  }

  private final Socket getSocket () {
    if (clientSocket == null) {
      Socket socket = new Socket();

      try {
        logConnectionState("connecting");
        socket.connect(makeServerAddress());
        logConnectionState("connected");
        clientSocket = socket;
      } catch (IOException exception) {
        Log.e(LOG_TAG, ("client connect error: " + exception.getMessage()));
      }
    } else if (clientSocket.isClosed()) {
      return null;
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
                RequestHandler handler;

                synchronized (DictionaryConnection.this) {
                  handler = responseQueue.peek();
                }

                if (handler == null) {
                  throw new OperandException("no request handler");
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

  private final void startRequestThread () {
    if (requestThread == null) {
      requestThread = new Thread("dictionary-request-thread") {
        @Override
        public void run () {
          Log.d(LOG_TAG, "request thread starting");

          try {
            StringBuilder command = new StringBuilder();

            while (true) {
              RequestEntry request;

              try {
                request = commandQueue.take();
                if (request == null) break;
              } catch (InterruptedException exception) {
                Log.w(LOG_TAG, "request thread interrupted");
                break;
              }

              String[] arguments = request.arguments;
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

                  responseQueue.offer(request.handler);
                  startResponseThread();
                }
              } catch (OperandException exception) {
                Log.w(LOG_TAG, exception.getMessage());
              }
            }
          } finally {
            Log.d(LOG_TAG, "request thread finished");
            close();
          }
        }
      };

      requestThread.start();
    }
  }

  public final void startCommand (RequestHandler handler, String... arguments) {
    startRequestThread();
    commandQueue.offer(new RequestEntry(handler, arguments));
  }
}
