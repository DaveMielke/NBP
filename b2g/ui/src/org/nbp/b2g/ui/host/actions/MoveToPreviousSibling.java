package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToPreviousSibling extends ScreenAction {
  @Override
  public boolean performAction () {
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

  public MoveToPreviousSibling () {
    super(true);
  }
}
