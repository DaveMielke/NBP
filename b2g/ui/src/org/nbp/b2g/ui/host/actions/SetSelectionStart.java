package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionStart extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int start = getSelectionOffset(cursorKey);

          if (isCharacterOffset(start)) {
            int end = BrailleDevice.getSelectionEnd();
            if (!BrailleDevice.isSelected(end) || (end <= start)) end = start + 1;
            if (connection.setSelection(start, end)) return true;
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionStart (Endpoint endpoint) {
    super(endpoint);
  }
}
