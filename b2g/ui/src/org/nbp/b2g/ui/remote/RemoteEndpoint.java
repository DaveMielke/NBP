package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class RemoteEndpoint extends Endpoint {
  private final Protocol protocol;

  public RemoteEndpoint () {
    super("remote");

    protocol = new BaumProtocol(this);
  }
}
