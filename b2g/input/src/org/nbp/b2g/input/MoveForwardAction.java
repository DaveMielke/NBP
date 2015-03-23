package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveForwardAction extends MoveAction {
  private boolean moveToNode (AccessibilityNodeInfo node) {
    if (setCurrentNode(node)) return true;
    return moveToDescendant(node);
  }

  private boolean moveToDescendant (AccessibilityNodeInfo node, int index) {
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

  private boolean moveToDescendant (AccessibilityNodeInfo node) {
    return moveToDescendant(node, 0);
  }

  @Override
  protected boolean moveToNextNode (AccessibilityNodeInfo node, boolean inner) {
    node = AccessibilityNodeInfo.obtain(node);

    if (moveToDescendant(node)) {
      node.recycle();
      return true;
    }

    while (true) {
      if (inner && node.isFocusable()) break;

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
  protected int getScrollAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_FORWARD;
  }

  public MoveForwardAction () {
    super();
  }
}
