package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public class BackwardAction extends MoveAction {
  private static final String LOG_TAG = BackwardAction.class.getName();

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
      if (inner && node.isFocusable()) break;

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
  protected int getScrollAction () {
    return AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD;
  }

  public BackwardAction () {
    super("BACKWARD");
  }
}
