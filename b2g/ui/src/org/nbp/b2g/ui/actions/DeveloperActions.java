package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperActions extends Action {
  @Override
  public boolean performAction (boolean isLongPress) {
    Control control = Controls.getDeveloperActionsControl();
    return isLongPress? control.nextValue(): control.previousValue();
  }

  public DeveloperActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}
