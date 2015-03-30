package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class DeletePrevious extends ScanCodeAction {
  @Override
  public boolean performAction () {
    InputConnection connection = getInputConnection();
    if (connection == null) return super.performAction();
    return connection.deleteSurroundingText(1, 0);
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
