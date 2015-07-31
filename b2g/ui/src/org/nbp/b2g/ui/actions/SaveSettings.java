package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SaveSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.saveValues();
    ApplicationUtilities.message(R.string.SaveSettings_action_confirmation);
    return true;
  }

  public SaveSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
