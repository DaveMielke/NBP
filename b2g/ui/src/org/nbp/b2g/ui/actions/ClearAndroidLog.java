package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ClearAndroidLog extends Action {
  @Override
  public boolean performAction () {
    return LogProcessor.clear();
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.ClearAndroidLog_action_confirmation;
  }

  public ClearAndroidLog (Endpoint endpoint) {
    super(endpoint, true);
  }
}
