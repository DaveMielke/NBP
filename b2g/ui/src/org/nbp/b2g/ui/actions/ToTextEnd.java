package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.util.Log;

public class ToTextEnd extends Action {
  private final static String LOG_TAG = ToTextEnd.class.getName();

  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int textOffset = endpoint.getTextLength();

      if (endpoint.isInputArea()) {
        endpoint.setCursor(textOffset);
        return true;
      } else {
        endpoint.setLine(textOffset);
        int end = endpoint.getLineLength();
        int length = Devices.braille.get().getLength();
        int indent = endpoint.getAdjustedLineOffset(-length, end);
        if (indent < 0) indent = 0;
        endpoint.setLineIndent(indent);
        return endpoint.write();
      }
    }
  }

  public ToTextEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
