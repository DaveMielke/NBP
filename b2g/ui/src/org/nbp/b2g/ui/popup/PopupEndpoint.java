package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;

public class PopupEndpoint extends Endpoint {
  private Timeout timeout = new Timeout(ApplicationParameters.BRAILLE_POPUP_TIMEOUT, "popup-timeout") {
    @Override
    public void run () {
      Endpoints.setHostEndpoint();
    }
  };

  @Override
  protected boolean braille () {
    timeout.start();
    return super.braille();
  }

  @Override
  public void onBackground () {
    timeout.cancel();
    super.onBackground();
  }

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
