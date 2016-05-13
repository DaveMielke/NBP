package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class RemoteEndpoint extends Endpoint {
  private final Channel channel;
  private final Protocol protocol;

  public final Channel getChannel () {
    return channel;
  }

  public final Protocol getProtocol () {
    return protocol;
  }

  public RemoteEndpoint () {
    super("remote");

    channel = new BluetoothChannel(this);
    protocol = new BaumProtocol(this);

    write("offline");
    channel.start();
  }
}
