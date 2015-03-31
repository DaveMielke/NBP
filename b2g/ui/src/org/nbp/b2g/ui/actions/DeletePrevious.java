package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeletePrevious extends InputAction {
  @Override
  public boolean performAction () {
    synchronized (BrailleDevice.LOCK) {
      if (ScreenUtilities.isEditable()) {
        InputService service = getInputService();

        if (service != null) {
          InputConnection connection = service.getCurrentInputConnection();

          if (connection != null) {
            int start = service.getSelectionStart();
            int end = service.getSelectionEnd();

            if (isSelected(start, end)) {
              return deleteText(connection, start, end);
            } else {
              return connection.deleteSurroundingText(1, 0);
            }
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
    super();
  }
}
