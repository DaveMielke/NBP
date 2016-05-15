package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.Closeable;

import java.io.InputStream;
import java.io.BufferedInputStream;

import java.io.OutputStream;
import java.io.BufferedOutputStream;

import java.util.UUID;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;

public class BluetoothChannel extends Channel {
  private final static String LOG_TAG = BluetoothChannel.class.getName();

  public BluetoothChannel (DisplayEndpoint endpoint) {
    super(endpoint);
  }

  private final static Object STOP_LOCK = new Object();
  private boolean stopFlag;
  private Closeable currentSocket;

  private final boolean setCurrentSocket (Closeable socket) {
    synchronized (STOP_LOCK) {
      if (stopFlag) return false;
      currentSocket = socket;
      return true;
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
      Log.w(LOG_TAG, ("server socket creation error: " + exception.getMessage()));
    }

    return null;
  }

  private final void closeServerSocket (BluetoothServerSocket socket) {
    closeObject(socket, "Bluetooth server socket");
  }

  private static BluetoothSocket getSessionSocket (BluetoothServerSocket server) {
    try {
      return server.accept();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("session socket creation error: " + exception.getMessage()));
    }

    return null;
  }

  private final void closeSessionSocket (BluetoothSocket socket) {
    closeObject(socket, "Bluetooth session socket");
  }

  private static InputStream getInputStream (BluetoothSocket socket) {
    try {
      return socket.getInputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("input stream creation error: " + exception.getMessage()));
    }

    return null;
  }

  private static OutputStream getOutputStream (BluetoothSocket socket) {
    try {
      return socket.getOutputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("output stream creation error: " + exception.getMessage()));
    }

    return null;
  }

  private OutputStream outputStream;

  @Override
  protected final void runChannelThread () {
    while (setCurrentSocket(null)) {
      BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

      if (adapter != null) {
        write("Bluetooth waiting");
        BluetoothServerSocket server = getServerSocket(adapter);

        if (server != null) {
          if (!setCurrentSocket(server)) {
            closeServerSocket(server);
            break;
          }

          Log.d(LOG_TAG, "channel listening");
          BluetoothSocket session = getSessionSocket(server);

          closeServerSocket(server);
          server = null;

          if (session != null) {
            if (!setCurrentSocket(session)) {
              closeSessionSocket(session);
              break;
            }

            Log.d(LOG_TAG, "channel connected");
            InputStream inputStream = getInputStream(session);

            if (inputStream != null) {
              if ((outputStream = getOutputStream(session)) != null) {
                outputStream = new BufferedOutputStream(outputStream);
                handleInput(new BufferedInputStream(inputStream));

                closeObject(outputStream, "Bluetooth output stream");
                outputStream = null;
              }

              closeObject(inputStream, "Bluetooth input stream");
              inputStream = null;
            }

            closeSessionSocket(session);
            session = null;
          }

          continue;
        }

        write("Bluetooth failure");
      } else {
        write("Bluetooth off");
        Log.w(LOG_TAG, "no default adapter");
      }

      if (!setCurrentSocket(null)) break;
      ApplicationUtilities.sleep(ApplicationParameters.BLUETOOTH_RETRY_INTERVAL);
    }
  }

  @Override
  protected final void initializeChannelThread () {
    stopFlag = false;
    currentSocket = null;
    outputStream = null;
  }

  @Override
  protected final void stopChannelThread () {
    synchronized (STOP_LOCK) {
      stopFlag = true;

      if (currentSocket != null) {
        closeObject(currentSocket, "current socket");
        currentSocket = null;
      }
    }
  }

  @Override
  public final boolean send (byte b) {
    if (outputStream == null) return true;

    try {
      outputStream.write(b & BYTE_MASK);
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("channel write error: " + exception.getMessage()));
    }

    return false;
  }

  @Override
  public final boolean flush () {
    try {
      outputStream.flush();
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("channel flush error: " + exception.getMessage()));
    }

    return false;
  }
}
