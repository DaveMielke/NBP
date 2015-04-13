package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CopyToClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();
    String text = endpoint.getSelectedText();
    if (text == null) text = endpoint.getText();

    if (text != null) {
      if (Clipboard.putText(text)) {
        return true;
      }
    }

    return false;
  }

  public CopyToClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
