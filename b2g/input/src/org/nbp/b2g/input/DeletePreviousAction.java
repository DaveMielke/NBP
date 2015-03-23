package org.nbp.b2g.input;

import android.view.KeyEvent;

public class DeletePreviousAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "BACKSPACE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DEL;
  }

  public DeletePreviousAction () {
    super();
  }
}
