package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToParent extends ScreenAction {
  @Override
  public boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      AccessibilityNodeInfo parent = node.getParent();

      if (parent != null) {
        if (setCurrentNode(parent, true)) moved = true;
        parent.recycle();
      }

      node.recycle();
    }

    return true;
  }

  public MoveToParent () {
    super();
  }
}
