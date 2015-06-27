package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;

public class PopupEndpoint extends Endpoint {
  private final static String[] keysFileNames = new String[] {
    "nabcc", "common", "speech", "popup"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public PopupEndpoint () {
    super();
  }
}
