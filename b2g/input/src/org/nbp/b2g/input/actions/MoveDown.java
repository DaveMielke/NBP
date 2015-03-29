package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveDown extends ScreenAction {
  @Override
  public final boolean performAction () {
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

  public MoveDown () {
    super();
  }
}
