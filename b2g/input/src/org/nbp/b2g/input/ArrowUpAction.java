package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowUpAction extends ArrowAction {
  @Override
  protected String getScanCode () {
    return "UP";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_UP;
  }

  public ArrowUpAction () {
    super();
  }
}
