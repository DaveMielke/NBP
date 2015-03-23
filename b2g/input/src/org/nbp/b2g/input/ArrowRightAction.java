package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowRightAction extends ArrowAction {
  @Override
  protected String getScanCode () {
    return "RIGHT";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_RIGHT;
  }

  public ArrowRightAction () {
    super();
  }
}
