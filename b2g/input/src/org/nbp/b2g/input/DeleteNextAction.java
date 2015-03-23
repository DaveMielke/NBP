package org.nbp.b2g.input;

import android.view.KeyEvent;

public class DeleteNextAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "DELETE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_FORWARD_DEL;
  }

  public DeleteNextAction () {
    super();
  }
}
