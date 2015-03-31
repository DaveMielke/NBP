package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetSelectionEnd extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputService service = getInputService();

        if (service != null) {
          InputConnection connection = service.getCurrentInputConnection();

          if (connection != null) {
            int end = getOffset(cursorKey);

            if (isCharacterOffset(end)) {
              int start = service.getSelectionStart();
              if ((start == InputService.NO_SELECTION) || (start > end)) start = end;
              if (connection.setSelection(start, end+1)) return true;
            }
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public SetSelectionEnd () {
    super();
  }
}
