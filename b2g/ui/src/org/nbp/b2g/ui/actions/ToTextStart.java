package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class ToTextStart extends Action {
  private final static String LOG_TAG = ToTextStart.class.getName();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int textOffset = 0;

      if (endpoint.isInputArea()) {
        endpoint.setCursor(textOffset);
        return true;
      } else {
        endpoint.setLine(textOffset);
        endpoint.setLineIndent(0);
        return endpoint.write();
      }
    }
  }

  public ToTextStart (Endpoint endpoint) {
    super(endpoint, false);
  }
}
