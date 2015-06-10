package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CursorRight extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_cursorRight();
  }

  public CursorRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
