package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class ToSelectionEnd extends Action {
  private final static String LOG_TAG = ToSelectionEnd.class.getName();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int textOffset = endpoint.getSelectionEnd();
        endpoint.setLine(textOffset);
        endpoint.setLineIndent(endpoint.toLineOffset(textOffset));
        return endpoint.write();
      }
    }

    return true;
  }

  public ToSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
