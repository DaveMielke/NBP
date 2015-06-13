package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.Closeable;

import java.io.InputStream;
import java.io.OutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.util.UUID;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public abstract class BrailleDisplay extends Thread {
  private final static String LOG_TAG = BrailleDisplay.class.getName();

  private OutputStream outputStream = null;

  protected boolean write (int b) {
    try {
      outputStream.write(b);
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth write error: " + exception.getMessage());
    }

    return false;
  }

  protected boolean flush () {
    try {
      outputStream.flush();
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth flush error: " + exception.getMessage());
    }

    return false;
  }

  protected abstract void resetInput (boolean readTimedOut);
  protected abstract boolean handleInput (int b);

  protected static void logIgnoredByte (int b) {
    Log.w(LOG_TAG, String.format("input byte ignored: 0X%02X", b));
  }

  private static void closeObject (Closeable object, String description) {
    try {
      object.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("%s close error: %s", description, exception.getMessage()), exception);
    }
  }

  private final static UUID SERIAL_PROFILE_UUID = UUID.fromString(
    "00001101-0000-1000-8000-00805F9B34FB"
  );

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

  private Timeout readTimeout = new Timeout(ApplicationParameters.BLUETOOTH_READ_TIMEOUT, "braille-display-read-timeout") {
    @Override
    public void run () {
      synchronized (this) {
        Log.w(LOG_TAG, "bluetooth read timeout");
        resetInput(true);
      }
    }
  };

  private void handleInput (InputStream stream) {
    resetInput(false);

    while (true) {
      int b;

      try {
        b = stream.read();
        readTimeout.cancel();
      } catch (IOException exception) {
        Log.w(LOG_TAG, "bluetooth input error: " + exception.getMessage());
        break;
      }

      if (b == -1) {
        Log.w(LOG_TAG, "end of bluetooth input");
        break;
      }

      if (!handleInput(b)) {
        readTimeout.start();
      } else if (!flush()) {
        break;
      }
    }

    readTimeout.cancel();
  }

  @Override
  public final void run () {
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
              if ((outputStream = getOutputStream(sessionSocket)) != null) {
                outputStream = new BufferedOutputStream(outputStream);
                handleInput(new BufferedInputStream(inputStream));

                closeObject(outputStream, "bluetooth output stream");
                outputStream = null;
              }

              closeObject(inputStream, "bluetooth input stream");
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
