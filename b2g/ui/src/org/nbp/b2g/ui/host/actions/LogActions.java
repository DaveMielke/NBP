package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogActions extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_PERFORMED_ACTIONS = true;
    message("log actions");
    return true;
  }

  public LogActions (Endpoint endpoint) {
    super(endpoint, true);
  }
}
