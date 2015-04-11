package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogNavigation extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_SCREEN_NAVIGATION = true;
    return true;
  }

  public LogNavigation (Endpoint endpoint) {
    super(endpoint, true);
  }
}
