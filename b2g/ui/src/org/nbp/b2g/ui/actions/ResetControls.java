package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetControls extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreDefaultValues();
    ApplicationUtilities.message(R.string.resetControls_action_confirmation);
    return true;
  }

  public ResetControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
