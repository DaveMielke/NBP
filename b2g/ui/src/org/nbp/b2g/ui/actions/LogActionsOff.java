package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogActionsOff extends PreviousValueAction {
  public LogActionsOff (Endpoint endpoint) {
    super(endpoint, Controls.logActions, true);
  }
}
