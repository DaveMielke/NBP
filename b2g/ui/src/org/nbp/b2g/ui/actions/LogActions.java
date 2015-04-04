package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogActions extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.LOG_PERFORMED_ACTIONS = !ApplicationParameters.LOG_PERFORMED_ACTIONS;
    return true;
  }

  public LogActions () {
    super(true);
  }
}
