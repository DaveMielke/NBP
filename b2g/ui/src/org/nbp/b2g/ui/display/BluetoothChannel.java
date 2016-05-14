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

public class BluetoothChannel extends Channel {
  private final static String LOG_TAG = BluetoothChannel.class.getName();

  public BluetoothChannel (DisplayEndpoint endpoint) {
    super(endpoint);
  }

  private final static UUID SERIAL_PROFILE_UUID = UUID.fromString(
    "00001101-0000-1000-8000-00805F9B34FB"
  );

  private OutputStream outputStream = null;

  @Override
  public final boolean send (byte b) {
    if (outputStream == null) return true;

    try {
      outputStream.write(b & BYTE_MASK);
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("Bluetooth channel write error: " + exception.getMessage()));
    }

    return false;
  }

  @Override
  public final boolean flush () {
    try {
      outputStream.flush();
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("Bluetooth channel flush error: " + exception.getMessage()));
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
      Log.w(LOG_TAG, ("Bluetooth channel server creation error: " + exception.getMessage()));
    }

    return null;
  }

  private static BluetoothSocket getSessionSocket (BluetoothServerSocket server) {
    try {
      return server.accept();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("Bluetooth channel sdession creation error: " + exception.getMessage()));
    }

    return null;
  }

  private static InputStream getInputStream (BluetoothSocket socket) {
    try {
      return socket.getInputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("Bluetooth channel input stream creation error: " + exception.getMessage()));
    }

    return null;
  }

  private static OutputStream getOutputStream (BluetoothSocket socket) {
    try {
      return socket.getOutputStream();
    } catch (IOException exception) {
      Log.w(LOG_TAG, ("Bluetooth channel output stream creation error: " + exception.getMessage()));
    }

    return null;
  }

  @Override
  public final void run () {
    Log.d(LOG_TAG, "Bluetooth channel starting");

    while (!Thread.interrupted()) {
      BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

      if (adapter != null) {
        displayEndpoint.write("Bluetooth waiting");
        BluetoothServerSocket server = getServerSocket(adapter);

        if (server != null) {
          Log.d(LOG_TAG, "Bluetooth channel listening");
          BluetoothSocket session = getSessionSocket(server);

          closeObject(server, "bluetooth server socket");
          server = null;

          if (session != null) {
            Log.d(LOG_TAG, "Bluetooth channel connected");
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
      } else {
        displayEndpoint.write("Bluetooth off");
        Log.w(LOG_TAG, "no Bluetooth adapter");
      }
    }

    displayEndpoint.write("Bluetooth stopped");
    Log.d(LOG_TAG, "Bluetooth channel stopped");
  }
}
