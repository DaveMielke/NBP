package org.nbp.b2g.ui;

import android.view.accessibility.AccessibilityNodeInfo;

public enum ScrollDirection {
  FORWARD  (AccessibilityNodeInfo.ACTION_SCROLL_FORWARD),
  BACKWARD (AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);

  private final int nodeAction;

  public final int getNodeAction () {
    return nodeAction;
  }

  private ScrollDirection (int nodeAction) {
    this.nodeAction = nodeAction;
  }
}
