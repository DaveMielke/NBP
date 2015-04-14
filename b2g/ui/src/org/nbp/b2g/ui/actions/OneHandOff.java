package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class OneHandOff extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.ONE_HAND_MODE = false;
    message("one hand", ApplicationParameters.ONE_HAND_MODE);
    return true;
  }

  public OneHandOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
