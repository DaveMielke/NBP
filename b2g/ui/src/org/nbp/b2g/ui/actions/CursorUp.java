package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CursorUp extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_cursorUp();
  }

  public CursorUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
