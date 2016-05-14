package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import java.io.IOException;

import java.io.InputStream;
import java.io.BufferedInputStream;

import java.io.OutputStream;
import java.io.BufferedOutputStream;

import java.util.UUID;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;

public class BluetoothChannel extends Channel implements Runnable {
  private final static String LOG_TAG = BluetoothChannel.class.getName();

  public BluetoothChannel (DisplayEndpoint endpoint) {
    super(endpoint);
  }

  private final static UUID SERIAL_PROFILE_UUID = UUID.fromString(
    "00001101-0000-1000-8000-00805F9B34FB"
  );

  private Thread channelThread = null;
  private OutputStream outputStream = null;

  @Override
  public final void start () {
    channelThread = new Thread(this, "bluetooth-braille-display");
    channelThread.start();
  }

  @Override
  public final void stop () {
  }

  @Override
  public final boolean send (byte b) {
    if (outputStream == null) return true;

    try {
      outputStream.write(b & BYTE_MASK);
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth write error: " + exception.getMessage());
    }

    return false;
  }

  @Override
  public final boolean flush () {
    try {
      outputStream.flush();
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, "bluetooth flush error: " + exception.getMessage());
    }

    return false;
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

  @Override
  public final void run () {
    Log.d(LOG_TAG, "bluetooth server started");
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    if (adapter != null) {
      while (true) {
        displayEndpoint.write("Bluetooth waiting");
        BluetoothServerSocket server = getServerSocket(adapter);

        if (server != null) {
          Log.d(LOG_TAG, "bluetooth server listening");
          BluetoothSocket session = getSessionSocket(server);

          closeObject(server, "bluetooth server socket");
          server = null;

          if (session != null) {
            Log.d(LOG_TAG, "bluetooth server connected");
            InputStream inputStream = getInputStream(session);

            if (inputStream != null) {
              if ((outputStream = getOutputStream(session)) != null) {
                outputStream = new BufferedOutputStream(outputStream);
                handleInput(new BufferedInputStream(inputStream));

                closeObject(outputStream, "bluetooth output stream");
                outputStream = null;
              }

              closeObject(inputStream, "bluetooth input stream");
              inputStream = null;
            }

            closeObject(session, "bluetooth session socket");
            session = null;
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
}
