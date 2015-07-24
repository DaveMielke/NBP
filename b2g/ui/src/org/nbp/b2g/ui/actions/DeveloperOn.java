package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperOn extends Action {
  @Override
  public boolean performAction () {
    Control control = Controls.getDeveloperActionsControl();
    return control.nextValue();
  }

  public DeveloperOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
