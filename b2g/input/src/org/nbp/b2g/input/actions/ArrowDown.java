package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowDown extends ArrowAction {
  @Override
  protected String getScanCode () {
    return "DOWN";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_DOWN;
  }

  public ArrowDown () {
    super();
  }
}
