package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.KeyEvent;

public class MenuKey extends KeyCodeAction {
  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MENU;
  }

  public MenuKey () {
    super();
  }
}
