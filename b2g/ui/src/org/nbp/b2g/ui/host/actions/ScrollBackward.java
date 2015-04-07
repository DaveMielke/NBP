package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollBackward extends NodeAction {
  @Override
  protected int getNodeAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;
  }

  public ScrollBackward (Endpoint endpoint) {
    super(endpoint, true);
  }
}
