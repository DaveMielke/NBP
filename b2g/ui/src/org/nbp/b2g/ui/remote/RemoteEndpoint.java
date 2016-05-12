package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class RemoteEndpoint extends Endpoint {
  public RemoteEndpoint () {
    super("remote");

    if (ApplicationParameters.ENABLE_BLUETOOTH_SERVER) {
      new BaumBrailleDisplay().start();
    }
  }
}
