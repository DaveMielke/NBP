package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperOff extends Action {
  @Override
  public boolean performAction () {
    Control control = Controls.getDeveloperActionsControl();
    return control.previousValue();
  }

  public DeveloperOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
