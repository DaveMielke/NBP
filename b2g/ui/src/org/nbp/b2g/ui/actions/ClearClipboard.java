package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ClearClipboard extends Action {
  @Override
  public boolean performAction () {
    return Clipboard.putText("");
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.ClearClipboard_action_confirmation;
  }

  public ClearClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
