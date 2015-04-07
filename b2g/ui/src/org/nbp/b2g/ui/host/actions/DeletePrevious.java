package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeletePrevious extends InputAction {
  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputConnection connection = getInputConnection();

        if (connection != null) {
          if (BrailleDevice.isSelected()) {
            return deleteSelectedText(connection);
          } else {
            return connection.deleteSurroundingText(1, 0);
          }
        }
      }
    }

    return super.performAction();
  }

  @Override
  protected String getScanCode () {
    return "BACKSPACE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DEL;
  }

  public DeletePrevious () {
    super(false);
  }
}
