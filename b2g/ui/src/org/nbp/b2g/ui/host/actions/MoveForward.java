package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveForward extends MoveAction {
  private final boolean moveToNode (AccessibilityNodeInfo node) {
    if (setCurrentNode(node)) return true;
    return moveToDescendant(node);
  }

  private final boolean moveToDescendant (AccessibilityNodeInfo node, int index) {
    int count = node.getChildCount();

    while (index < count) {
      AccessibilityNodeInfo child = node.getChild(index++);

      if (child != null) {
        boolean moved = moveToNode(child);
        child.recycle();
        if (moved) return true;
      }
    }

    return false;
  }

  private final boolean moveToDescendant (AccessibilityNodeInfo node) {
    return moveToDescendant(node, 0);
  }

  @Override
  protected final boolean moveToNextNode (AccessibilityNodeInfo node, boolean inner) {
    node = AccessibilityNodeInfo.obtain(node);

    if (moveToDescendant(node)) {
      node.recycle();
      return true;
    }

    while (true) {
      if (inner && node.isScrollable()) break;

      AccessibilityNodeInfo parent = node.getParent();
      if (parent == null) break;

      int myChildIndex = findChildIndex(parent, node);
      node.recycle();
      node = parent;
      parent = null;

      if (myChildIndex >= 0) {
        if (moveToDescendant(node, myChildIndex+1)) {
          node.recycle();
          return true;
        }
      }
    }

    node.recycle();
    return false;
  }

  @Override
  protected final ScrollDirection getScrollDirection () {
    return ScrollDirection.FORWARD;
  }

  public MoveForward (Endpoint endpoint) {
    super(endpoint, false);
  }
}
