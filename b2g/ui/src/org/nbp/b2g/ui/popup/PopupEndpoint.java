package org.nbp.b2g.ui.popup;
import org.nbp.b2g.ui.*;
import org.nbp.b2g.ui.actions.*;

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

  @Override
  public Class<? extends Action> getMoveBackwardAction () {
    return LinePrevious.class;
  }

  @Override
  public Class<? extends Action> getMoveForwardAction () {
    return LineNext.class;
  }

  public PopupEndpoint () {
    super("popup");
  }
}
