package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetSettings extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreDefaultValues();
    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.ResetSettings_action_confirmation;
  }

  public ResetSettings (Endpoint endpoint) {
    super(endpoint, true);
  }
}
