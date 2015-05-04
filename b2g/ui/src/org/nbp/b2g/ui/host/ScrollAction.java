package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScrollAction extends ScreenAction {
  protected abstract ScrollDirection getScrollDirection ();
  protected abstract boolean getContinueScrolling ();

  @Override
  public boolean performAction () {
    AccessibilityNodeInfo oldCurrentNode = getCurrentNode();

    if (oldCurrentNode != null) {
      ScrollContainer container = ScrollContainer.findContainer(oldCurrentNode);

      if (container != null) {
        int oldChildIndex = container.findContainingChild(oldCurrentNode);
        int newChildIndex = -1;

        ScrollDirection direction = getScrollDirection();
        boolean hasScrolled = false;

        if (getContinueScrolling()) {
          while (container.scroll(direction)) {
            hasScrolled = true;
          }
        } else if (container.scroll(direction)) {
          hasScrolled = true;
          newChildIndex = oldChildIndex;
        }

        if (hasScrolled) {
          container.deselectDescendants();
        }

        if (newChildIndex == -1) {
          switch (direction) {
            case FORWARD:
              newChildIndex = container.getChildCount() - 1;
              break;

            case BACKWARD:
              newChildIndex = 0;
              break;

            default:
              break;
          }

          if (!hasScrolled) {
            if (newChildIndex == oldChildIndex) {
              newChildIndex = -1;
            }
          }
        }

        if (newChildIndex != -1) {
          newChildIndex = Math.min(newChildIndex, (container.getChildCount() - 1));

          if (newChildIndex >= 0) {
            AccessibilityNodeInfo child = container.getChild(newChildIndex);

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
