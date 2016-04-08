package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BringCursor extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    if (endpoint.isInputArea()) {
      offset += endpoint.getLineStart();
      return endpoint.setCursor(offset);
    }

    {
      Action action = endpoint.getKeyBindings().getAction(KeyMask.DPAD_CENTER);

      if (action != null) {
        return KeyEvents.performAction(action);
      }
    }

    return false;
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint, true);
  }
}
