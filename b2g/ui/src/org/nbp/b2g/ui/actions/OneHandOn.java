package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOn extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.ONE_HAND_MODE = true;
    message("one hand", ApplicationParameters.ONE_HAND_MODE);
    return true;
  }

  public OneHandOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
