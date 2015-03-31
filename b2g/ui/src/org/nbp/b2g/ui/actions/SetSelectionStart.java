package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionStart extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputService service = getInputService();

        if (service != null) {
          InputConnection connection = service.getCurrentInputConnection();

          if (connection != null) {
            int start = getOffset(cursorKey);

            if (isCharacterOffset(start)) {
              int end = service.getSelectionEnd();
              if ((end == InputService.NO_SELECTION) || (end <= start)) end = start + 1;
              if (connection.setSelection(start, end)) return true;
            }
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionStart () {
    super();
  }
}
