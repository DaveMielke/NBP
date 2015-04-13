package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public abstract class InputAction extends HostAction {
  protected boolean deleteText (InputConnection connection, int start, int end) {
    if (connection.beginBatchEdit()) {
      if (getEndpoint().setCursor(end)) {
        if (connection.deleteSurroundingText((end - start), 0)) {
          if (connection.endBatchEdit()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  protected boolean deleteSelectedText (InputConnection connection) {
    Endpoint endpoint = getEndpoint();
    return deleteText(connection, endpoint.getSelectionStart(), endpoint.getSelectionEnd());
  }

  protected InputAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
