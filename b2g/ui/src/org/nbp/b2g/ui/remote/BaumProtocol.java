package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

public class BaumProtocol extends Protocol {
  private final static byte ESCAPE = Characters.CHAR_ESC;

  public BaumProtocol (RemoteEndpoint endpoint) {
    super(endpoint);
  }
}
