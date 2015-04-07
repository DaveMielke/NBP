package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionEnd extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int end = getSelectionOffset(cursorKey);

          if (isCharacterOffset(end)) {
            int start = BrailleDevice.getSelectionStart();
            if (!BrailleDevice.isSelected(start) || (start > end)) start = end;
            if (connection.setSelection(start, end+1)) return true;
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
