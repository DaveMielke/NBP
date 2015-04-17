package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class MoveAction extends ScreenAction {
  private final static String LOG_TAG = MoveAction.class.getName();

  protected abstract boolean moveToNextNode (AccessibilityNodeInfo node, boolean force);
  protected abstract int getScrollAction ();

  protected boolean scroll (AccessibilityNodeInfo node) {
    if (ScreenUtilities.isSeekable(node)) return false;
    boolean scrolled = performNodeAction(node, getScrollAction());

    if (scrolled) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("scroll succeeded");
      ApplicationUtilities.sleep(ApplicationParameters.SCREEN_SCROLL_DELAY);
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("scroll failed");
    }

    return scrolled;
  }

  protected boolean innerMove (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, true);

    if (moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move succeeded");
      performNodeAction(node, AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("inner move failed");
    }

    return moved;
  }

  protected boolean outerMove (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, false);

    if (moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("outer move failed");
    }

    return moved;
  }

  @Override
  public boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "starting node");
      if (innerMove(node)) moved = true;
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("no starting node");
      return false;
    }

    if (!moved) {
      if (scroll(node)) {
        {
          AccessibilityNodeInfo found = findNode(node);
          node.recycle();

          if (found != null) {
            if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(found, "found node after scroll");
            node = found;
            found = null;
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
          if (innerMove(node)) moved = true;
        }
      }
    }

    if (!moved) {
      if (outerMove(node)) moved = true;
    }

    if (!moved) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("move failed");
    }

    node.recycle();
    return moved;
  }

  protected MoveAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
