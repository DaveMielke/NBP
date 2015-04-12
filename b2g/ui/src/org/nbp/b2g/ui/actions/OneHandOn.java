package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOn extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.ONE_HAND_MODE = true;
    return true;
  }

  public OneHandOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
