package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeleteNext extends ScanCodeAction {
  @Override
  public boolean performAction () {
    InputService service = getInputService();

    if (service != null) {
      InputConnection connection = service.getCurrentInputConnection();

      if (connection != null) {
        if (connection.deleteSurroundingText(0, 1)) {
          return true;
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
