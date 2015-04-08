package org.nbp.b2g.ui.bluetooth;
import org.nbp.b2g.ui.*;

public class BluetoothEndpoint extends Endpoint {
  public BluetoothEndpoint () {
    super();
    new BrailleDisplay().start();
  }
}
