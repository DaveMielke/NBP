package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToSelf extends ScreenAction {
  @Override
  public boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      if (setCurrentNode(node, true)) moved = true;
      node.recycle();
    }

    return moved;
  }

  public MoveToSelf (Endpoint endpoint) {
    super(endpoint, true);
  }
}
