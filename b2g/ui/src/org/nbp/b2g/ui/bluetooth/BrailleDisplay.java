package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BrailleDisplay extends Thread {
  private final static String LOG_TAG = BrailleDisplay.class.getName();

  private OutputStream outputStream = null;

  private final static UUID SERIAL_PROFILE_UUID = UUID.fromString(
    "00001101-0000-1000-8000-00805F9B34FB"
  );

  private static void closeObject (Closeable object, String description) {
    try {
      object.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("%s close error: %s", description, exception.getMessage()), exception);
    }
  }

  private static BluetoothServerSocket getServerSocket (BluetoothAdapter adapter) {
    try {
      String name = ApplicationParameters.BLUETOOTH_SERVICE_NAME;
      UUID uuid = SERIAL_PROFILE_UUID;

      if (ApplicationParameters.BLUETOOTH_SECURE_CONNECTION) {
        return adapter.listenUsingRfcommWithServiceRecord(name, uuid);
      } else {
        return adapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid);
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth server socket creation error: " + exception.getMessage());
    }

    return null;
  }

  private static BluetoothSocket getSessionSocket (BluetoothServerSocket server) {
    try {
      return server.accept();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth sdession socket creation error: " + exception.getMessage());
    }

    return null;
  }

  private static InputStream getInputStream (BluetoothSocket socket) {
    try {
      return socket.getInputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth input stream creation error: " + exception.getMessage());
    }

    return null;
  }

  private static OutputStream getOutputStream (BluetoothSocket socket) {
    try {
      return socket.getOutputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth output stream creation error: " + exception.getMessage());
    }

    return null;
  }

  protected static void logIgnoredByte (byte b) {
    Log.w(LOG_TAG, String.format("input byte ignored: 0X%02X", b));
  }

  protected void onByteReceived (byte b) {
    logIgnoredByte(b);
  }

  private void processInput (InputStream stream) {
    while (true) {
      int b;

      try {
        b = stream.read();
        if (b == -1) break;
      } catch (IOException exception) {
        Log.w(LOG_TAG, "bluetooth input error: " + exception.getMessage());
        break;
      }

      onByteReceived((byte)b);
    }
  }

  @Override
  public void run () {
    Log.d(LOG_TAG, "bluetooth server started");
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    if (adapter != null) {
      while (true) {
        BluetoothServerSocket serverSocket = getServerSocket(adapter);

        if (serverSocket != null) {
          Log.d(LOG_TAG, "bluetooth server listening");
          BluetoothSocket sessionSocket = getSessionSocket(serverSocket);

          closeObject(serverSocket, "bluetooth server socket");
          serverSocket = null;

          if (sessionSocket != null) {
            Log.d(LOG_TAG, "bluetooth server connected");
            InputStream inputStream = getInputStream(sessionSocket);

            if (inputStream != null) {
              synchronized (this) {
                outputStream = getOutputStream(sessionSocket);
              }

              if (outputStream != null) {
                processInput(inputStream);

                synchronized (this) {
                  outputStream = null;
                }
              }
            }

            closeObject(sessionSocket, "bluetooth session socket");
            sessionSocket = null;
          }
        } else {
          ApplicationUtilities.sleep(ApplicationParameters.BLUETOOTH_RETRY_INTERVAL);
        }
      }
    } else {
      Log.w(LOG_TAG, "no bluetooth adapter");
    }

    Log.d(LOG_TAG, "bluetooth server stopped");
  }

  public BrailleDisplay () {
    super("bluetooth-braille-display");
  }
}
