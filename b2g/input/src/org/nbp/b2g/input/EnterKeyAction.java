package org.nbp.b2g.input;

import android.view.KeyEvent;

public class EnterKeyAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "ENTER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ENTER;
  }

  public EnterKeyAction () {
    super();
  }
}
