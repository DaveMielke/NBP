package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.util.UUID;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BrailleDisplay extends Thread {
  private final static String LOG_TAG = BrailleDisplay.class.getName();

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
          String name = ApplicationParameters.BLUETOOTH_SERVICE_NAME;
          UUID uuid = SERIAL_PROFILE_UUID;

          if (ApplicationParameters.BLUETOOTH_SECURE_CONNECTION) {
            server = adapter.listenUsingRfcommWithServiceRecord(name, uuid);
          } else {
            server = adapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid);
          }
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

  public BrailleDisplay () {
    super("bluetooth-braille-display");
  }
}
