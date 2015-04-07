package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.content.ClipboardManager;
import android.content.ClipData;

import android.view.inputmethod.InputConnection;

public class CutToClipboard extends InputAction {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        String text = endpoint.getSelectedText();

        if (text != null) {
          InputConnection connection = getInputConnection();

          if (connection != null) {
            if (copyToClipboard(text)) {
              if (deleteSelectedText(connection)) {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  public CutToClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
