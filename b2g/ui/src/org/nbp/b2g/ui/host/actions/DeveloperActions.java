package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DeveloperActions extends Action {
  @Override
  public boolean performAction () {
    ApplicationParameters.ENABLE_DEVELOPER_ACTIONS = !ApplicationParameters.ENABLE_DEVELOPER_ACTIONS;
    return true;
  }

  public DeveloperActions () {
    super(false);
  }
}
