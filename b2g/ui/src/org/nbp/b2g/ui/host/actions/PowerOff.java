package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import org.nbp.common.CommonUtilities;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class PowerOff extends GlobalAction {
  @Override
  protected int getGlobalAction () {
    if (CommonUtilities.haveLollipop) {
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

  public PowerOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
