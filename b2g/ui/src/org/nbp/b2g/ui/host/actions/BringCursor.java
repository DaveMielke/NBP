package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class BringCursor extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int offset = getSelectionOffset(cursorKey);

          if (isCursorOffset(offset)) {
            if (connection.setSelection(offset, offset)) {
              return true;
            }
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint);
  }
}
