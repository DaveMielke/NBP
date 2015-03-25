package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.KeyEvent;

public class DeletePrevious extends ScanCodeAction {
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
