package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CursorKey extends CursorKeyAction {
  private final static KeySet centerKey = new KeySet() {
    {
      set(PAD_CENTER);
    }
  };

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    if (endpoint.isInputArea()) {
      offset += endpoint.getLineStart();
      return endpoint.setCursor(offset);
    }

    {
      Action action = endpoint.getKeyBindings().getAction(centerKey);
      if (action != null) return KeyEvents.performAction(action);
    }

    return false;
  }

  public CursorKey (Endpoint endpoint) {
    super(endpoint, true);
  }
}
