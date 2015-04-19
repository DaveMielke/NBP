package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class RestoreControls extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreControls();
    ApplicationUtilities.message(R.string.restore_action_confirmation);
    return true;
  }

  public RestoreControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
