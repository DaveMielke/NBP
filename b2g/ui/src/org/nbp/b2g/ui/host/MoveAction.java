package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class MoveAction extends ScreenAction {
  private final static String LOG_TAG = MoveAction.class.getName();

  protected abstract boolean moveToNextNode (AccessibilityNodeInfo node, boolean force);
  protected abstract ScrollDirection getScrollDirection ();

  protected boolean scroll (AccessibilityNodeInfo node) {
    boolean scrolled = false;
    AccessibilityNodeInfo view = ScreenUtilities.findScrollable(node);

    if (view != null) {
      if (ScreenMonitor.scrollView(view, getScrollDirection())) {
        ScreenUtilities.logNavigation("scroll succeeded");
        scrolled = true;
      } else {
        ScreenUtilities.logNavigation("scroll failed");
      }

      view.recycle();
    }

    return scrolled;
  }

  protected boolean innerMove (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, true);

    if (moved) {
      ScreenUtilities.logNavigation("inner move succeeded");
      performNodeAction(node, AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);
    } else {
      ScreenUtilities.logNavigation("inner move failed");
    }

    return moved;
  }

  protected boolean outerMove (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, false);

    if (moved) {
      ScreenUtilities.logNavigation("outer move succeeded");
    } else {
      ScreenUtilities.logNavigation("outer move failed");
    }

    return moved;
  }

  @Override
  public boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      ScreenUtilities.logNavigation(node, "starting node");
      if (innerMove(node)) moved = true;
    } else {
      ScreenUtilities.logNavigation("no starting node");
      return false;
    }

    if (!moved) {
      if (scroll(node)) {
        {
          AccessibilityNodeInfo found = findNode(node);
          node.recycle();

          if (found != null) {
            ScreenUtilities.logNavigation(found, "found node after scroll");
            node = found;
            found = null;
          } else {
            ScreenUtilities.logNavigation("node not found after scroll");
            node = ScreenUtilities.getCurrentNode();

            if (node != null) {
              ScreenUtilities.logNavigation(node, "current node after scroll");
            } else {
              ScreenUtilities.logNavigation("no current node after scroll");
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
      ScreenUtilities.logNavigation("move failed");
    }

    node.recycle();
    return moved;
  }

  protected MoveAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
