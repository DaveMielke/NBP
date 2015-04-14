package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LongPressOff extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LONG_PRESS_ACTIONS = false;
    message("long press", ApplicationParameters.LONG_PRESS_ACTIONS);
    return true;
  }

  public LongPressOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
