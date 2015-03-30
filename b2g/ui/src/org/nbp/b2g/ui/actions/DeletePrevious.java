package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeletePrevious extends ScanCodeAction {
  @Override
  public boolean performAction () {
    InputService service = getInputService();

    if (service != null) {
      InputConnection connection = service.getCurrentInputConnection();

      if (connection != null) {
        if (connection.deleteSurroundingText(1, 0)) {
          return true;
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
