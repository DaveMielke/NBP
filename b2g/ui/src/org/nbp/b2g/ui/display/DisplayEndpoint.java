package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

public class DisplayEndpoint extends Endpoint {
  private final Channel currentChannel;
  private final Protocol currentProtocol;

  public final Channel getChannel () {
    return currentChannel;
  }

  public final Protocol getProtocol () {
    return currentProtocol;
  }

  public final boolean start () {
    return currentChannel.start();
  }

  public final boolean stop () {
    return currentChannel.stop();
  }

  @Override
  public void onBackground () {
    try {
      currentProtocol.resetKeys();
    } finally {
      super.onBackground();
    }
  }

  @Override
  public final int handleNavigationKeys (int keyMask, boolean press) {
    return currentProtocol.handleNavigationKeys(keyMask, press);
  }

  @Override
  public final boolean handleCursorKey (int keyNumber, boolean press) {
    return currentProtocol.handleCursorKey(keyNumber, press);
  }

  public DisplayEndpoint () {
    super("display");
    write("offline");

    currentChannel = new BluetoothChannel();
    currentProtocol = new BaumProtocol();
  }
}
