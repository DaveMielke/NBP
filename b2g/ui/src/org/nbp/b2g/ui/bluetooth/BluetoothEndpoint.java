package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;

public class BluetoothEndpoint extends Endpoint {
  private final static String LOG_TAG = BluetoothEndpoint.class.getName();

  class ServerThread extends Thread {
    protected final UUID SERIAL_PROFILE_UUID = UUID.fromString(
      "00001101-0000-1000-8000-00805F9B34FB"
    );

    @Override
    public void run () {
      Log.d(LOG_TAG, "bluetooth server started");
      BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

      if (adapter != null) {
        while (true) {
          BluetoothServerSocket server = null;
          BluetoothSocket socket = null;

          try {
            server = adapter.listenUsingRfcommWithServiceRecord(
              ApplicationParameters.BLUETOOTH_SERVICE_NAME,
              SERIAL_PROFILE_UUID
            );
          } catch (IOException exception) {
            Log.w(LOG_TAG, "bluetooth server socket not created: " + exception.getMessage());
            ApplicationUtilities.sleep(ApplicationParameters.BLUETOOTH_RETRY_INTERVAL);
            continue;
          }

          try {
            Log.d(LOG_TAG, "bluetooth server listening");
            socket = server.accept();
            server.close();
            server = null;
            Log.d(LOG_TAG, "bluetooth server connected");
          } catch (IOException exception) {
            Log.w(LOG_TAG, "bluetooth socket not created: " + exception.getMessage());
          }
        }
      } else {
        Log.w(LOG_TAG, "no bluetooth adapter");
      }

      Log.d(LOG_TAG, "bluetooth server stopped");
    }
  }

  public BluetoothEndpoint () {
    super();
    new ServerThread().start();
  }
}
