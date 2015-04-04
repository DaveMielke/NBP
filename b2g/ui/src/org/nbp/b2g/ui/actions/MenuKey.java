package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MenuKey extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "MENU";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MENU;
  }

  public MenuKey () {
    super(false);
  }
}
