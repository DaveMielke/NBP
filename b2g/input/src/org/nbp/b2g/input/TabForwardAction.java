package org.nbp.b2g.input;

import android.view.KeyEvent;

public class TabForwardAction extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "TAB";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_TAB;
  }

  public TabForwardAction () {
    super();
  }
}
