package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigation extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_SCREEN_NAVIGATION = !ApplicationParameters.LOG_SCREEN_NAVIGATION;
    return true;
  }

  public LogNavigation () {
    super();
  }
}
