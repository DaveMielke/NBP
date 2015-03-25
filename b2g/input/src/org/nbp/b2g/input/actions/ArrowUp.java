package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowUp extends ArrowAction {
  @Override
  protected String getScanCode () {
    return "UP";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_UP;
  }

  public ArrowUp () {
    super();
  }
}
