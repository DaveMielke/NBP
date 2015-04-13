package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionEnd extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int end = endpoint.getTextOffset(cursorKey);

          if (endpoint.isCharacterOffset(end)) {
            int start = endpoint.getSelectionStart();
            if (!endpoint.isSelected(start) || (start > end)) start = end;
            if (connection.setSelection(start, end+1)) return true;
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
