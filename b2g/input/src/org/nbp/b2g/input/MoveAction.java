package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class MoveAction extends ScreenAction {
  private static final String LOG_TAG = MoveAction.class.getName();

  protected abstract boolean moveToNextNode (AccessibilityNodeInfo node, boolean force);
  protected abstract int getScrollAction ();

  @Override
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node;

    node = getCurrentNode();
    if (node == null) return false;

    if (moveToNextNode(node, false)) {
      moved = true;
    } else if (performAction(node, getScrollAction())) {
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

  public MoveAction (String name) {
    super(name);
  }
}
