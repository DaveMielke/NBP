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

        int end = endpoint.toLineOffset(textOffset);
        if (endpoint.hasCursor()) end += 1;

        int length = Devices.braille.get().getLength();
        int indent = endpoint.getAdjustedLineOffset(-length, end);
        if (indent < 0) indent = 0;
        endpoint.setLineIndent(indent);

        return endpoint.write();
      }
    }

    return true;
  }

  public ToSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
