package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

public class HostEndpoint extends Endpoint {
  private final static String[] keysFileNames = new String[] {
    "nabcc", "navigation", "developer"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public HostEndpoint () {
    super();
  }
}
