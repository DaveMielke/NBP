package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ClearAndroidLog extends Action {
  @Override
  public boolean performAction () {
    boolean cleared = LogProcessor.clear();
    if (cleared) ApplicationUtilities.message(R.string.ClearAndroidLog_action_confirmation);
    return cleared;
  }

  public ClearAndroidLog (Endpoint endpoint) {
    super(endpoint, true);
  }
}
