package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BringCursor extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    if (!endpoint.isInputArea()) return false;
    offset += endpoint.getLineStart();
    return endpoint.setCursor(offset);
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint, true);
  }
}
