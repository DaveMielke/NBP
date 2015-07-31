package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreDefaultValues();
    ApplicationUtilities.message(R.string.ResetSettings_action_confirmation);
    return true;
  }

  public ResetSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
