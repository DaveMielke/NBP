package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputHighlightOff extends Action {
  @Override
  public boolean performAction () {
    Controls.resetInputHighlighting();
    return true;
  }

  public InputHighlightOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
