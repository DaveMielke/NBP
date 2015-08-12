package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SaveSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.saveValues();
    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.SaveSettings_action_confirmation;
  }

  public SaveSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
