package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class HomeKey extends SystemAction {
  @Override
  protected int getGlobalAction () {
    return AccessibilityService.GLOBAL_ACTION_HOME;
  }

  @Override
  protected int getSystemKeyCode () {
    return KeyEvent.KEYCODE_HOME;
  }

  public HomeKey () {
    super(false);
  }
}
