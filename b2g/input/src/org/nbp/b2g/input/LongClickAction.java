package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;

public class LongClickAction extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_LONG_CLICK;
  }

  public LongClickAction () {
    super();
  }
}
