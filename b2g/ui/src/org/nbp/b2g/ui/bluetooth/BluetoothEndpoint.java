package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

public class BluetoothEndpoint extends Endpoint {
  public BluetoothEndpoint () {
    super();

    if (ApplicationParameters.ENABLE_BLUETOOTH_SERVER) {
      new BaumBrailleDisplay().start();
    }
  }
}
