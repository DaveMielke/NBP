package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogKeys extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_KEY_EVENTS = !ApplicationParameters.LOG_KEY_EVENTS;
    return true;
  }

  public LogKeys () {
    super();
  }
}
