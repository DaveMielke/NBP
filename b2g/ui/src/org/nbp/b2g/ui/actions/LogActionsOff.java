package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogActionsOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogActionsControl().previousValue();
  }

  public LogActionsOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
