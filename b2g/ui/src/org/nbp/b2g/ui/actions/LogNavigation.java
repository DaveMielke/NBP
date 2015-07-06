package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigation extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogNavigationControl().nextValue();
  }

  public LogNavigation (Endpoint endpoint) {
    super(endpoint, true);
  }
}
