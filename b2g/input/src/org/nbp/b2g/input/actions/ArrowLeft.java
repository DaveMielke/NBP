package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowLeft extends ArrowAction {
  @Override
  protected String getScanCode () {
    return "LEFT";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_LEFT;
  }

  public ArrowLeft () {
    super();
  }
}
