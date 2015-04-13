package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionStart extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int start = endpoint.getTextOffset(cursorKey);

          if (endpoint.isCharacterOffset(start)) {
            int end = endpoint.getSelectionEnd();
            if (!endpoint.isSelected(end) || (end <= start)) end = start + 1;
            if (connection.setSelection(start, end)) return true;
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionStart (Endpoint endpoint) {
    super(endpoint, false);
  }
}
