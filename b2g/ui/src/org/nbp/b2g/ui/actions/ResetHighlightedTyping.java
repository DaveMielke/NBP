package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetHighlightedTyping extends Action {
  @Override
  public boolean performAction () {
    Controls.resetHighlightedTyping();
    return true;
  }

  public ResetHighlightedTyping (Endpoint endpoint) {
    super(endpoint, false);
  }
}
