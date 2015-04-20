package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SaveControls extends Action {
  @Override
  public boolean performAction () {
    Controls.saveValues();
    ApplicationUtilities.message(R.string.save_action_confirmation);
    return true;
  }

  public SaveControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
