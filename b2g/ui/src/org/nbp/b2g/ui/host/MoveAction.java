package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class MoveAction extends ScreenAction {
  private final static String LOG_TAG = MoveAction.class.getName();

  protected abstract boolean moveToNextNode (AccessibilityNodeInfo node, boolean inner);
  protected abstract ScrollDirection getScrollDirection ();

  protected boolean innerMove (AccessibilityNodeInfo node) {
    boolean moved = moveToNextNode(node, true);

    if (moved) {
      ScreenUtilities.logNavigation("inner move succeeded");
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
      ScrollContainer container = ScrollContainer.findContainer(node);

      if (container != null) {
        ScrollDirection direction = getScrollDirection();

        if (container.scroll(direction)) {
          ScreenUtilities.logNavigation("scroll succeeded");

          {
            AccessibilityNodeInfo found = findNode(node);

            node.recycle();
            node = null;

            if (found != null) {
              ScreenUtilities.logNavigation(found, "found node after scroll");
              node = found;
              found = null;
            } else {
              ScreenUtilities.logNavigation("node not found after scroll");
              int childIndex;

              switch (direction) {
                case FORWARD:
                  childIndex = 0;
                  break;

                case BACKWARD:
                  childIndex = container.getChildCount() - 1;
                  break;

                default:
                  childIndex = -1;
                  break;
              }

              if (childIndex >= 0) {
                AccessibilityNodeInfo child = container.getChild(childIndex);

                if (child != null) {
                  node = ScreenUtilities.findSignificantNode(child);
                  child.recycle();
                }
              }

              if (node != null) {
                ScreenUtilities.logNavigation(node, "new node after scroll");
                if (setCurrentNode(node)) moved = true;
              } else {
                ScreenUtilities.logNavigation("no node after scroll");
                return false;
              }
            }
          }

          if (!moved) {
            if (innerMove(node)) moved = true;
          }
        } else {
          ScreenUtilities.logNavigation("scroll failed");
          setCurrentNode(node);
        }
      } else {
        ScreenUtilities.logNavigation("not scrollable");
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

  protected MoveAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
