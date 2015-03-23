package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollBackwardAction extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;
  }

  public ScrollBackwardAction () {
    super();
  }
}
