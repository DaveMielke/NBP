package org.nbp.b2g.input;

import android.view.KeyEvent;

public class TabBackwardAction extends ScanCodeAction {
  private static final int[] scanCodeModifiers = new int[] {
    SCAN_CODE_SHIFT
  };

  @Override
  protected int[] getScanCodeModifiers () {
    return scanCodeModifiers;
  }

  @Override
  protected String getScanCode () {
    return "TAB";
  }

  private static final int[] keyCodeModifiers = new int[] {
    KEY_CODE_SHIFT
  };

  @Override
  protected int[] getKeyCodeModifiers () {
    return keyCodeModifiers;
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_TAB;
  }

  public TabBackwardAction () {
    super();
  }
}
