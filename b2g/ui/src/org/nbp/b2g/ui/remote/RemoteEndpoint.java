package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class RemoteEndpoint extends Endpoint {
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
    boolean stopped = currentChannel.stop();
    write("remote display off");
    return stopped;
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
  public final boolean handleNavigationKeyEvent (int key, boolean press) {
    return currentProtocol.handleNavigationKeyEvent(key, press);
  }

  @Override
  public final boolean handleCursorKeyEvent (int key, boolean press) {
    return currentProtocol.handleCursorKeyEvent(key, press);
  }

  public RemoteEndpoint () {
    super(false);
    addKeyBindings("remote");

    currentChannel = new BluetoothChannel();
    currentProtocol = new BaumProtocol();
  }
}
