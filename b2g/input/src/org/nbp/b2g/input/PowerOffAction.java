package org.nbp.b2g.input;

import android.os.Build;
import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class PowerOffAction extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.LOLLIPOP)) {
      return AccessibilityService.GLOBAL_ACTION_POWER_DIALOG;
    } else {
      return NULL_GLOBAL_ACTION;
    }
  }

  @Override
  protected String getScanCode () {
    return "POWER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_POWER;
  }

  @Override
  protected long getHoldTime () {
    return ApplicationUtilities.getGlobalActionTimeout();
  }

  public PowerOffAction () {
    super();
  }
}
