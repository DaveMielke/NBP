package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SelectAll extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int start = 0;
        int end = endpoint.getTextLength();

        if (end > start) {
          if (endpoint.setSelection(start, end)) {
            return true;
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
