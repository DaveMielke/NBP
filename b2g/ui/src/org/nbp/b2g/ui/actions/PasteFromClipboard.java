package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PasteFromClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        CharSequence text = Clipboard.getText();

        if (text != null) {
          if (endpoint.insertText(text)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public PasteFromClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
