package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNone extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_KEY_EVENTS = false;
    ApplicationParameters.LOG_PERFORMED_ACTIONS = false;
    ApplicationParameters.LOG_SCREEN_NAVIGATION = false;
    ApplicationParameters.LOG_ACCESSIBILITY_EVENTS = false;
    return true;
  }

  public LogNone () {
    super(true);
  }
}
