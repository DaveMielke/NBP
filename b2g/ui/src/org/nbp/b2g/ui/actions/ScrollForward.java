package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollForward extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
  }

  public ScrollForward () {
    super(true);
  }
}
