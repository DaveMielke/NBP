package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public class ForwardAction extends ScreenAction {
  private static final String LOG_TAG = ForwardAction.class.getName();

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

  protected boolean moveToNextNode (AccessibilityNodeInfo node, boolean force) {
    node = AccessibilityNodeInfo.obtain(node);

    if (moveToDescendant(node)) {
      node.recycle();
      return true;
    }

    while (true) {
      if (!force && node.isFocusable()) break;

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
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node;

    node = getCurrentNode();
    if (node == null) return false;

    if (moveToNextNode(node, false)) {
      moved = true;
    } else if (performAction(node, AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
      delay(ApplicationParameters.SCROLL_DELAY);
      node.recycle();
      node = getCurrentNode();
      if (node == null) return false;
      if (moveToNextNode(node, false)) moved = true;
    } else if (moveToNextNode(node, true)) {
      moved = true;
    }

    node.recycle();
    return moved;
  }

  public ForwardAction () {
    super("forward");
  }
}
