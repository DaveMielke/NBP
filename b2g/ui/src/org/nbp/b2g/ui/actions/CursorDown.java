package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CursorDown extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_cursorDown();
  }

  public CursorDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
