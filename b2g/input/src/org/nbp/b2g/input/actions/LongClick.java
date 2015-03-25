package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class LongClick extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_LONG_CLICK;
  }

  public LongClick () {
    super();
  }
}
