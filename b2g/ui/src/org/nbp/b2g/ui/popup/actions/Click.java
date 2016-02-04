package org.nbp.b2g.ui.popup.actions;
import org.nbp.b2g.ui.popup.*;
import org.nbp.b2g.ui.*;

public class Click extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleClick();
  }

  public Click (Endpoint endpoint) {
    super(endpoint, false);
  }
}
