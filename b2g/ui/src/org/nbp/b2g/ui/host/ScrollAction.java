package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScrollAction extends ScreenAction {
  private final static String LOG_TAG = ScrollAction.class.getName();

  protected abstract ScrollDirection getScrollDirection ();
  protected abstract boolean getContinueScrolling ();

  @Override
  public boolean performAction () {
    AccessibilityNodeInfo oldCurrentNode = getCurrentNode();

    if (oldCurrentNode != null) {
      ScrollContainer container = ScrollContainer.findContainer(oldCurrentNode);

      if (container != null) {
        int oldChildIndex = container.findChildIndex(oldCurrentNode);
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

        if (newChildIndex == -1) {
          newChildIndex = container.findChildIndex(direction);

          if (!hasScrolled) {
            if (newChildIndex == oldChildIndex) {
              newChildIndex = -1;
            }
          }
        }

        if (ApplicationParameters.CURRENT_LOG_NAVIGATION) {
          Log.d(LOG_TAG, String.format(
            "child index: %d -> %d", oldChildIndex, newChildIndex
          ));
        }

        if (newChildIndex != -1) {
          container.selectChild(newChildIndex);
          return true;
        }
      }
    }

    return false;
  }

  protected ScrollAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
