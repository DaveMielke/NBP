package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class CopyToClipboard extends InputAction {
  @Override
  public boolean performAction () {
    String text;

    {
      HostEndpoint endpoint = getHostEndpoint();

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
