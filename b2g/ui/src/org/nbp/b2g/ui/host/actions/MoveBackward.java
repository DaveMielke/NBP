package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveBackward extends MoveAction {
  private boolean moveToNode (AccessibilityNodeInfo node, int childIndex) {
    if (moveToDescendant(node, childIndex)) return true;
    return setCurrentNode(node);
  }

  private boolean moveToDescendant (AccessibilityNodeInfo node, int childIndex) {
    while (childIndex > 0) {
      AccessibilityNodeInfo child = node.getChild(--childIndex);

      if (child != null) {
        boolean moved = moveToNode(child, child.getChildCount());
        child.recycle();
        if (moved) return true;
      }
    }

    return false;
  }

  @Override
  protected boolean moveToNextNode (AccessibilityNodeInfo node, boolean inner) {
    node = AccessibilityNodeInfo.obtain(node);

    while (true) {
      if (inner && node.isScrollable()) break;

      AccessibilityNodeInfo parent = node.getParent();
      if (parent == null) break;

      int myChildIndex = findChildIndex(parent, node);
      node.recycle();
      node = parent;
      parent = null;

      if (moveToNode(node, myChildIndex)) {
        node.recycle();
        return true;
      }
    }

    node.recycle();
    return false;
  }

  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.BACKWARD;
  }

  public MoveBackward (Endpoint endpoint) {
    super(endpoint, false);
  }
}
