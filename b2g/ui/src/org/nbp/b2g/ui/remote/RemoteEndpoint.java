package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class RemoteEndpoint extends Endpoint {
  private final Channel channel;
  private final Protocol protocol;

  public final void resetInput (boolean timeout) {
    protocol.resetInput(timeout);
  }

  public final boolean handleInput (byte b) {
    return protocol.handleInput(b);
  }

  public RemoteEndpoint () {
    super("remote");

    channel = new BluetoothChannel(this);
    protocol = new BaumProtocol(this);

    channel.start();
  }
}
