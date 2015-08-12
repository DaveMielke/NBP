package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class RestoreSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreSavedValues();
    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.RestoreSettings_action_confirmation;
  }

  public RestoreSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
