package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogKeys extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_KEY_EVENTS = true;
    message("log keys");
    return true;
  }

  public LogKeys (Endpoint endpoint) {
    super(endpoint, true);
  }
}
