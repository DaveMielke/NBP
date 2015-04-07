package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SelectAll extends InputAction {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (ScreenUtilities.isEditable()) {
        int start = 0;
        int end = endpoint.getTextLength();

        if (end > start) {
          InputConnection connection = getInputConnection();

          if (connection != null) {
            if (connection.setSelection(start, end)) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  public SelectAll (Endpoint endpoint) {
    super(endpoint, false);
  }
}
