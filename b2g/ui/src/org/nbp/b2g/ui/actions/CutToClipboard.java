package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CutToClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        String text = endpoint.getSelectedText();

        if (text != null) {
          if (Clipboard.putText(text)) {
            if (endpoint.deleteSelectedText()) {
              ApplicationUtilities.message(R.string.CutToClipboard_action_confirmation);
              return true;
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
