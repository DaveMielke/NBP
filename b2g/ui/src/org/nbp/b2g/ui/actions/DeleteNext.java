package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeleteNext extends InputAction {
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
              return connection.deleteSurroundingText(0, 1);
            }
          }
        }
      }
    }

    return super.performAction();
  }

  @Override
  protected String getScanCode () {
    return "DELETE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_FORWARD_DEL;
  }

  public DeleteNext () {
    super();
  }
}
