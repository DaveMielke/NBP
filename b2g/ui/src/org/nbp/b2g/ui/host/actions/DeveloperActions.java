package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DeveloperActions extends Action {
  @Override
  public boolean performAction (boolean isLongPress) {
    ApplicationParameters.DEVELOPER_ACTIONS = isLongPress;
    message("developer", ApplicationParameters.DEVELOPER_ACTIONS);
    return true;
  }

  public DeveloperActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}
