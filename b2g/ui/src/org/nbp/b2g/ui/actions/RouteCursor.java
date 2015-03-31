package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class RouteCursor extends SetLeft {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      if (isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          int position = BrailleDevice.getIndent() + cursorKey;

          if (position <= BrailleDevice.getLength()) {
            if (connection.setSelection(position, position)) {
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
