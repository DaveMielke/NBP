package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class BackKeyAction extends SystemAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_BACK;
  }

  @Override
  protected int getSystemKeyCode () {
    return KeyEvent.KEYCODE_BACK;
  }

  public BackKeyAction () {
    super();
  }
}
