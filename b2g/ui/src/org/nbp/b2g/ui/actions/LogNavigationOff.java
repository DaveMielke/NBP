package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigationOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogNavigationControl().previousValue();
  }

  public LogNavigationOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
