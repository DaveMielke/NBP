package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CopyToClipboard extends Action {
  @Override
  public boolean performAction () {
    String text;

    {
      Endpoint endpoint = getEndpoint();

      synchronized (endpoint) {
        if (endpoint.isEditable() && endpoint.isSelected()) {
          text = endpoint.getSelectedText();
        } else {
          text = endpoint.getText();
        }
      }
    }

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
