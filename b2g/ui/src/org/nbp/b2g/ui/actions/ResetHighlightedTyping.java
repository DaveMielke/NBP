package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetHighlightedTyping extends Action {
  @Override
  public boolean performAction () {
    Controls.resetHighlightedTyping();
    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.ResetHighlightedTyping_action_confirmation;
  }

  public ResetHighlightedTyping (Endpoint endpoint) {
    super(endpoint, false);
  }
}
