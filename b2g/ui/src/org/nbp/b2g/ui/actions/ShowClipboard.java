package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ShowClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      CharSequence text = Clipboard.getText();

      if ((text != null) && (text.length() > 0)) {
        return Endpoints.setPopupEndpoint(text);
      }
    }

    return false;
  }

  public ShowClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
