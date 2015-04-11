package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class EnterKey extends FindAction {
  @Override
  public boolean performAction () {
    boolean found = false;
    Endpoint hostEndpoint = Endpoints.getHostEndpoint();

    FindEndpoint findEndpoint = getFindEndpoint();
    String response = findEndpoint.getResponse();

    synchronized (hostEndpoint) {
      int start = hostEndpoint.getBrailleStart();

      if (start < hostEndpoint.getTextLength()) {
        String text = hostEndpoint.getText();
        int offset = text.indexOf(response, start+1);

        if (offset >= 0) {
          hostEndpoint.setLineIndent(hostEndpoint.setLine(offset));
          found = true;
        }
      }
    }

    Endpoints.setHostEndpoint();
    return found;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint);
  }
}
