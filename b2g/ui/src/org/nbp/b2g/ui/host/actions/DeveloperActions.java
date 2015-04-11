package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DeveloperActions extends Action {
  @Override
  public boolean performAction (boolean isLongPress) {
    ApplicationParameters.ENABLE_DEVELOPER_ACTIONS = isLongPress;
    return true;
  }

  public DeveloperActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}
