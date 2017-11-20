package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypingHighlightOff extends Action {
  @Override
  public boolean performAction () {
    Controls.resetHighlightedTyping();
    return true;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.TypingHighlightOff_action_confirmation;
  }

  public TypingHighlightOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
