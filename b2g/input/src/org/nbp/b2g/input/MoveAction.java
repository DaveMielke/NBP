package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class MoveAction extends ScreenAction {
  private static final String LOG_TAG = MoveAction.class.getName();

  protected abstract boolean moveToNextNode (AccessibilityNodeInfo node, boolean force);
  protected abstract int getScrollAction ();

  public boolean innerMoveToNextNode (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, true);

    if (moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move failed");
    }

    return moved;
  }

  public boolean outerMoveToNextNode (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, false);

    if (moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move failed");
    }

    return moved;
  }

  @Override
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "starting node");
      if (innerMoveToNextNode(node)) moved = true;
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("no starting node");
      return false;
    }

    if (!moved) {
      if (performNodeAction(node, getScrollAction())) {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("scroll succeeded");
        ApplicationUtilities.sleep(ApplicationParameters.SCROLL_DELAY);

        {
          AccessibilityNodeInfo found = findNode(node);
          node.recycle();

          if (found != null) {
            if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(found, "node after scroll");
            node = found;
          } else {
            if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node not found after scroll");
            node = getCurrentNode();

            if (node != null) {
              if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "current node after scroll");
            } else {
              if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("no current node after scroll");
              return false;
            }

            if (setCurrentNode(node)) moved = true;
          }
        }

        if (!moved) {
          if (innerMoveToNextNode(node)) moved = true;
        }
      } else {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("scroll failed");
      }
    }

    if (!moved) {
      if (outerMoveToNextNode(node)) moved = true;
    }

    if (!moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("move failed");
    }

    node.recycle();
    return moved;
  }

  public MoveAction (String name) {
    super("MOVE_" + name);
  }
}
