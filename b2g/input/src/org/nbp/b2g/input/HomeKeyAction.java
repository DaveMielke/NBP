package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class HomeKeyAction extends SystemAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_HOME;
  }

  @Override
  protected int getSystemKeyCode () {
    return KeyEvent.KEYCODE_HOME;
  }

  public HomeKeyAction () {
    super();
  }
}
