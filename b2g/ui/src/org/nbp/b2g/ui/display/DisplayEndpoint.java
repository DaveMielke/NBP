package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

public class DisplayEndpoint extends Endpoint {
  private final Channel channel;
  private final Protocol protocol;

  public final Channel getChannel () {
    return channel;
  }

  public final Protocol getProtocol () {
    return protocol;
  }

  @Override
  public void onBackground () {
    try {
      protocol.resetKeys();
    } finally {
      super.onBackground();
    }
  }

  @Override
  public final int handleNavigationKeys (int keyMask, boolean press) {
    return protocol.handleNavigationKeys(keyMask, press);
  }

  @Override
  public final boolean handleCursorKey (int keyNumber, boolean press) {
    return protocol.handleCursorKey(keyNumber, press);
  }

  public DisplayEndpoint () {
    super("display");

    channel = new BluetoothChannel(this);
    protocol = new BaumProtocol(this);

    write("offline");
    channel.start();
  }
}
