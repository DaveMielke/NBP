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
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "starting node");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("no starting node");
      return false;
    }

    if (moveToNextNode(node, true)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move succeeded");
      moved = true;
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move failed");
    }

    if (!moved) {
      int attempts = 0;

      while (true) {
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
        } else {
          if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("scroll failed");
          break;
        }

        if (!moved) {
          if (moveToNextNode(node, true)) {
            if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move succeeded");
            moved = true;
          } else {
            if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move failed");
          }
        }

        if (moved) break;
        if ((attempts += 1) == 2) break;
      }
    }

    if (!moved) {
      if (moveToNextNode(node, false)) {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move succeeded");
        moved = true;
      } else {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move failed");
      }
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
