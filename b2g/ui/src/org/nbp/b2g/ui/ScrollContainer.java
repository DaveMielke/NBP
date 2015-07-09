package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.graphics.Point;
import android.graphics.Rect;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollContainer {
  private final static String LOG_TAG = ScrollContainer.class.getName();

  private static class ScrollContainers extends HashMap<AccessibilityNodeInfo, ScrollContainer> {
  }

  private final static ScrollContainers scrollContainers = new ScrollContainers();

  public static ScrollContainer getContainer (AccessibilityNodeInfo node) {
    synchronized (scrollContainers) {
      ScrollContainer container = scrollContainers.get(node);

      if (container == null) {
        node = AccessibilityNodeInfo.obtain(node);
        container = new ScrollContainer(node);
        scrollContainers.put(node, container);
      }

      return container;
    }
  }

  public static ScrollContainer findContainer (AccessibilityNodeInfo node) {
    if (node == null) return null;
    ScreenUtilities.logNavigation(node, "finding scrollable node");

    if (!ScreenUtilities.isSeekable(node)) {
      AccessibilityNodeInfo container = AccessibilityNodeInfo.obtain(node);

      do {
        if (container.isScrollable()) {
          ScreenUtilities.logNavigation(container, "found scrollable node");
          return getContainer(container);
        }

        AccessibilityNodeInfo parent = container.getParent();
        container.recycle();
        container = parent;
      } while (container != null);
    }

    ScreenUtilities.logNavigation(node, "not scrollable");
    return null;
  }

  private AccessibilityNodeInfo scrollNode = null;

  public AccessibilityNodeInfo getNode () {
    return AccessibilityNodeInfo.obtain(scrollNode);
  }

  public int getChildCount () {
    return scrollNode.getChildCount();
  }

  public AccessibilityNodeInfo getChild (int index) {
    return scrollNode.getChild(index);
  }

  private int itemCount = 0;

  public int getItemCount () {
    return itemCount;
  }

  public void setItemCount (int count) {
    itemCount = count;
  }

  private int firstItemIndex = -1;

  public int getFirstItemIndex () {
    return firstItemIndex;
  }

  public void setFirstItemIndex (int index) {
    firstItemIndex = index;
  }

  private int lastItemIndex = -1;

  public int getLastItemIndex () {
    return lastItemIndex;
  }

  public void setLastItemIndex (int index) {
    lastItemIndex = index;
  }

  private static boolean isVisible (AccessibilityNodeInfo root) {
    if (!root.isVisibleToUser()) return false;
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        boolean visible = isVisible(child);
        child.recycle();
        if (!visible) return false;
      }
    }

    return true;
  }

  public void selectChild (int childIndex) {
    ScreenUtilities.selectChild(scrollNode, childIndex);
  }

  private static int findChildIndex (AccessibilityNodeInfo node, AccessibilityNodeInfo root) {
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        boolean found = false;

        if (child.equals(node)) {
          found = true;
        } else if (findChildIndex(node, child) != -1) {
          found = true;
        }

        child.recycle();
        if (found) return childIndex;
      }
    }

    return -1;
  }

  public int findChildIndex (AccessibilityNodeInfo node) {
    return findChildIndex(node, scrollNode);
  }

  public int findChildIndex (ScrollDirection direction) {
    synchronized (this) {
      int from;
      int to;
      int increment;

      switch (direction) {
        case FORWARD:
          from = scrollNode.getChildCount() - 1;
          to = -1;
          increment = -1;
          break;

        case BACKWARD:
          from =  0;
          to = scrollNode.getChildCount();
          increment = 1;
          break;

        default:
          Log.w(LOG_TAG, "unimplemented scroll direction: " + direction.name());
          return -1;
      }

      while (from != to) {
        AccessibilityNodeInfo child = scrollNode.getChild(from);

        if (child != null) {
          boolean visible = isVisible(child);
          child.recycle();
          if (visible) return from;
        }

        from += increment;
      }
    }

    return -1;
  }

  public int findFirstChildIndex () {
    return findChildIndex(ScrollDirection.BACKWARD);
  }

  public int findLastChildIndex () {
    return findChildIndex(ScrollDirection.FORWARD);
  }

  private boolean refreshNode () {
    synchronized (this) {
      AccessibilityNodeInfo node = ScreenUtilities.getRefreshedNode(scrollNode);

      if (node != null) {
        synchronized (scrollContainers) {
          scrollContainers.remove(scrollNode);
          scrollNode.recycle();

          scrollNode = node;
          scrollContainers.put(node, this);
        }

        ScreenUtilities.logNavigation(node, "refresh succeeded");
        return true;
      } else {
        ScreenUtilities.logNavigation(node, "refresh failed");
      }
    }

    return false;
  }

  private boolean scrollTimeout = false;

  public boolean scroll (ScrollDirection direction) {
    boolean scrolled = false;

    synchronized (this) {
      refreshNode();
      selectChild(findChildIndex(direction));
      scrollTimeout = true;
      boolean scrollStarted = false;

      if (Gesture.isEnabled()) {
        if (ScreenUtilities.hasAction(scrollNode, direction.getNodeAction())) {
          direction.writeBrailleSymbol();

          Rect region = ScreenUtilities.getNodeRegion(scrollNode);
          int x = (region.left + region.right) / 2;
          int y1 = region.top;

          int dy = region.bottom - region.top - 1;
          dy = Math.min(dy, Math.max(dy/2, 50));
          int y2 = y1 + dy;

          switch (direction) {
            case FORWARD: {
              int y = y1;
              y1 = y2;
              y2 = y;
              break;
            }

            case BACKWARD:
              break;

            default:
              Log.w(LOG_TAG, "unimplemented scroll direction: " + direction.name());
              return false;
          }

          if (Gesture.swipe(x, y1, x, y2)) {
            scrollStarted = true;
          }
        }
      } else {
        direction.writeBrailleSymbol();
        if (scrollNode.performAction(direction.getNodeAction())) scrollStarted = true;
      }

      if (scrollStarted) {
        ScreenUtilities.logNavigation(scrollNode, "scroll started");

        try {
          wait(ApplicationParameters.VIEW_SCROLL_TIMEOUT);

          if (scrollTimeout) {
            ScreenUtilities.logNavigation(scrollNode, "scroll timeout");
          } else {
            ScreenUtilities.logNavigation(scrollNode, "scroll finished");
            scrolled = true;
          }

          refreshNode();
        } catch (InterruptedException exception) {
          ScreenUtilities.logNavigation(scrollNode, "scroll interrupted");
        }
      } else {
        ScreenUtilities.logNavigation(scrollNode, "scroll failed");
      }
    }

    return scrolled;
  }

  public void onScroll () {
    synchronized (this) {
      scrollTimeout = false;
      notify();
    }
  }

  private ScrollContainer (AccessibilityNodeInfo node) {
    scrollNode = node;
  }
}
