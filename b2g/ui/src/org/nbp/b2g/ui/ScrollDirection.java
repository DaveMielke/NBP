package org.nbp.b2g.ui;

import android.view.accessibility.AccessibilityNodeInfo;

public enum ScrollDirection {
  FORWARD,
  BACKWARD;

  public static int getNodeAction (ScrollDirection direction) {
    switch (direction) {
      case FORWARD:  return AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
      case BACKWARD: return AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;
      default: throw new IllegalArgumentException(direction.name());
    }
  }

  public final int getNodeAction () {
    return getNodeAction(this);
  }
}
