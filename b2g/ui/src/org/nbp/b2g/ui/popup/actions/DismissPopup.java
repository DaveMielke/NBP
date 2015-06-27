package org.nbp.b2g.ui.popup.actions;
import org.nbp.b2g.ui.popup.*;
import org.nbp.b2g.ui.*;

public class DismissPopup extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setHostEndpoint();
    return true;
  }

  public DismissPopup (Endpoint endpoint) {
    super(endpoint, false);
  }
}
