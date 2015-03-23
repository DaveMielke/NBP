package org.nbp.b2g.input;

import android.view.KeyEvent;

public class MenuKeyAction extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MENU;
  }

  public MenuKeyAction () {
    super();
  }
}
