package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToChild extends ScreenAction {
  @Override
  public boolean performAction () {
    Boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      if (node.getChildCount() > 0) {
        AccessibilityNodeInfo child = node.getChild(0);

        if (child != null) {
          if (setCurrentNode(child, true)) moved = true;
          child.recycle();
        }
      }

      node.recycle();
    }

    return moved;
  }

  public MoveToChild () {
    super();
  }
}
