package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MovePrevious extends ScreenAction {
  @Override
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      AccessibilityNodeInfo parent = node.getParent();

      if (parent != null) {
        int childIndex = findChildIndex(parent, node);

        if (--childIndex >= 0) {
          AccessibilityNodeInfo child = parent.getChild(childIndex);

          if (child != null) {
            if (setCurrentNode(child, true)) moved = true;
            child.recycle();
          }
        }

        parent.recycle();
      }

      node.recycle();
    }

    return moved;
  }

  public MovePrevious () {
    super();
  }
}
