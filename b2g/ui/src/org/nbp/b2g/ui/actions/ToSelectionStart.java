package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class ToSelectionStart extends Action {
  private final static String LOG_TAG = ToSelectionStart.class.getName();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int textOffset = endpoint.getSelectionStart();
        endpoint.setLine(textOffset);
        endpoint.setLineIndent(endpoint.toLineOffset(textOffset));
        return endpoint.write();
      }
    }

    return true;
  }

  public ToSelectionStart (Endpoint endpoint) {
    super(endpoint, false);
  }
}
