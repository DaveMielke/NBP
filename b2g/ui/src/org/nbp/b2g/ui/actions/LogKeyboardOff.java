package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogKeyboardOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogKeyboardControl().previousValue();
  }

  public LogKeyboardOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
