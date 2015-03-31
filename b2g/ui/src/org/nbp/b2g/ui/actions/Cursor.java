package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class Cursor extends ScreenAction {
  @Override
  public boolean performAction (int cursorKey) {
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
    } else {
      if (BrailleDevice.shiftRight(cursorKey)) return true;
    }

    return false;
  }

  public Cursor () {
    super();
  }
}
