package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class RouteCursor extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int offset = getSelectionOffset(cursorKey);

          if (isCursorOffset(offset)) {
            if (connection.setSelection(offset, offset)) {
              return true;
            }
          }
        }

        return false;
      }
    }

    return super.performAction(cursorKey);
  }

  public RouteCursor () {
    super();
  }
}
