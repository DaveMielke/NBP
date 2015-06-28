package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class RestoreSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreSavedValues();
    ApplicationUtilities.message(R.string.restoreSettings_action_confirmation);
    return true;
  }

  public RestoreSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
