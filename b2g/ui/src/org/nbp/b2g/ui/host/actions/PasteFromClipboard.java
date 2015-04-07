package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class PasteFromClipboard extends InputAction {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        String text = Clipboard.getText();

        if (text != null) {
          InputService service = getInputService();

          if (service != null) {
            if (service.insert(text)) {
              return true;
            }
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
