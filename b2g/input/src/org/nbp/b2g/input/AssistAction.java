package org.nbp.b2g.input;

import android.view.KeyEvent;

public class AssistAction extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ASSIST;
  }

  public AssistAction () {
    super();
  }
}
