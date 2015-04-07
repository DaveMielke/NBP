package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class BackKey extends SystemAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_BACK;
  }

  @Override
  protected int getSystemKeyCode () {
    return KeyEvent.KEYCODE_BACK;
  }

  public BackKey (Endpoint endpoint) {
    super(endpoint, false);
  }
}
