package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.KeyEvent;

public class Click extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_CLICK;
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_CENTER;
  }

  public Click () {
    super(false);
  }
}
