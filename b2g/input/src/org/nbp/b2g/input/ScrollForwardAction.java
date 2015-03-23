package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollForwardAction extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
  }

  public ScrollForwardAction () {
    super();
  }
}
