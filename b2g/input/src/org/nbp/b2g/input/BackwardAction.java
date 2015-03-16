package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public class BackwardAction extends ScreenAction {
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

  protected boolean moveToPreviousNode (AccessibilityNodeInfo node, boolean force) {
    node = AccessibilityNodeInfo.obtain(node);

    while (true) {
      if (!force && node.isFocusable()) break;

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
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node;

    node = getCurrentNode();
    if (node == null) return false;

    if (moveToPreviousNode(node, false)) {
      moved = true;
    } else if (performAction(node, AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)) {
      delay(ApplicationParameters.SCROLL_DELAY);
      node.recycle();
      node = getCurrentNode();
      if (node == null) return false;
      if (moveToPreviousNode(node, false)) moved = true;
    } else if (moveToPreviousNode(node, true)) {
      moved = true;
    }

    node.recycle();
    return moved;
  }

  public BackwardAction () {
    super("backward");
  }
}
