package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CursorLeft extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_cursorLeft();
  }

  public CursorLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
