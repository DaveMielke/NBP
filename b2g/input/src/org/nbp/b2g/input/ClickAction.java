package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.KeyEvent;

public class ClickAction extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_CLICK;
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_CENTER;
  }

  public ClickAction () {
    super();
  }
}
