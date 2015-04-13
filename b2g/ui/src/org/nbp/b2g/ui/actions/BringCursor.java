package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BringCursor extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        int offset = endpoint.getTextOffset(cursorKey);

        if (endpoint.isCursorOffset(offset)) {
          if (endpoint.setCursor(offset)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint, false);
  }
}
