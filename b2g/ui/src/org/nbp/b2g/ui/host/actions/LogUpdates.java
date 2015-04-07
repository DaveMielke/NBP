package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogUpdates extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_ACCESSIBILITY_EVENTS = !ApplicationParameters.LOG_ACCESSIBILITY_EVENTS;
    return true;
  }

  public LogUpdates (Endpoint endpoint) {
    super(endpoint, true);
  }
}
