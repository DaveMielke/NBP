package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScrollAction extends ScreenAction {
  protected abstract ScrollDirection getScrollDirection ();
  protected abstract boolean getContinueScrolling ();

  private final static int NULL_CHILD_INDEX = -1;

  private static int findContainingChild (AccessibilityNodeInfo root, AccessibilityNodeInfo node) {
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        boolean found = false;

        if (child.equals(node)) {
          found = true;
        } else if (findContainingChild(child, node) != NULL_CHILD_INDEX) {
          found = true;
        }

        child.recycle();
        if (found) return childIndex;
      }
    }

    return NULL_CHILD_INDEX;
  }

  private static void deselectDescendants (AccessibilityNodeInfo root) {
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        if (child.isSelected()) {
          child.performAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);
        }

        deselectDescendants(child);
        child.recycle();
      }
    }
  }

  @Override
  public boolean performAction () {
    AccessibilityNodeInfo oldCurrentNode = getCurrentNode();

    if (oldCurrentNode != null) {
      AccessibilityNodeInfo view = ScreenUtilities.findScrollable(oldCurrentNode);

      if (view != null) {
        int oldChildIndex = findContainingChild(view, oldCurrentNode);
        int newChildIndex = NULL_CHILD_INDEX;

        ScrollDirection direction = getScrollDirection();
        boolean hasScrolled = false;
        deselectDescendants(view);

        if (getContinueScrolling()) {
          while (ScreenMonitor.scrollView(view, direction)) {
            hasScrolled = true;
          }
        } else if (ScreenMonitor.scrollView(view, direction)) {
          hasScrolled = true;
          newChildIndex = oldChildIndex;
        }

        if (hasScrolled) {
          {
            AccessibilityNodeInfo newView = ScreenUtilities.getRefreshedNode(view);

            if (newView != null) {
              view.recycle();
              view = newView;
            }
          }

          deselectDescendants(view);
        }

        if (newChildIndex == NULL_CHILD_INDEX) {
          switch (direction) {
            case FORWARD:
              newChildIndex = view.getChildCount() - 1;
              break;

            case BACKWARD:
              newChildIndex = 0;
              break;

            default:
              break;
          }

          if (!hasScrolled) {
            if (newChildIndex == oldChildIndex) {
              newChildIndex = NULL_CHILD_INDEX;
            }
          }
        }

        if (newChildIndex != NULL_CHILD_INDEX) {
          newChildIndex = Math.min(newChildIndex, (view.getChildCount() - 1));

          if (newChildIndex >= 0) {
            AccessibilityNodeInfo child = view.getChild(newChildIndex);

            if (child != null) {
              AccessibilityNodeInfo newCurrentNode = ScreenUtilities.findCurrentNode(child);

              child.recycle();
              child = null;

              if (newCurrentNode != null) {
                boolean selected = setCurrentNode(newCurrentNode);
                newCurrentNode.recycle();
                if (selected) return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  protected ScrollAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
