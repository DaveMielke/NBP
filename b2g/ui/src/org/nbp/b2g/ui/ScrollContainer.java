package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.view.accessibility.AccessibilityNodeInfo;

public class ScrollContainer {
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
      AccessibilityNodeInfo view = AccessibilityNodeInfo.obtain(node);

      do {
        if (view.isScrollable()) {
          ScreenUtilities.logNavigation(view, "found scrollable node");
          return getContainer(view);
        }

        AccessibilityNodeInfo parent = view.getParent();
        view.recycle();
        view = parent;
      } while (view != null);
    }

    ScreenUtilities.logNavigation(node, "not scrollable");
    return null;
  }

  private AccessibilityNodeInfo scrollNode = null;
  private boolean scrollTimeout = false;

  private int itemCount = 0;
  private int firstItemIndex = -1;
  private int lastItemIndex = -1;

  public AccessibilityNodeInfo getNode () {
    return AccessibilityNodeInfo.obtain(scrollNode);
  }

  public int getChildCount () {
    return scrollNode.getChildCount();
  }

  public AccessibilityNodeInfo getChild (int index) {
    return scrollNode.getChild(index);
  }

  public int getItemCount () {
    return itemCount;
  }

  public int getFirstItemIndex () {
    return firstItemIndex;
  }

  public int getLastItemIndex () {
    return lastItemIndex;
  }

  public void setItemCount (int count) {
    itemCount = count;
  }

  public void setFirstItemIndex (int index) {
    firstItemIndex = index;
  }

  public void setLastItemIndex (int index) {
    lastItemIndex = index;
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

  public void deselectDescendants () {
    deselectDescendants(scrollNode);
  }

  private static int findContainingChild (AccessibilityNodeInfo node, AccessibilityNodeInfo root) {
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        boolean found = false;

        if (child.equals(node)) {
          found = true;
        } else if (findContainingChild(node, child) != -1) {
          found = true;
        }

        child.recycle();
        if (found) return childIndex;
      }
    }

    return -1;
  }

  public int findContainingChild (AccessibilityNodeInfo node) {
    return findContainingChild(node, scrollNode);
  }

  private boolean refreshScrollNode () {
    synchronized (this) {
      AccessibilityNodeInfo node = ScreenUtilities.getRefreshedNode(scrollNode);
      if (node == null) return false;

      scrollNode.recycle();
      scrollNode = node;

      synchronized (scrollContainers) {
        scrollContainers.put(node, this);
      }
    }

    return true;
  }

  public boolean scroll (ScrollDirection direction) {
    boolean scrolled = false;

    synchronized (this) {
      deselectDescendants();
      scrollTimeout = true;

      if (scrollNode.performAction(direction.getNodeAction())) {
        ScreenUtilities.logNavigation(scrollNode, "scroll started");

        try {
          wait(ApplicationParameters.VIEW_SCROLL_DELAY);
          scrolled = true;

          if (scrollTimeout) {
            ScreenUtilities.logNavigation(scrollNode, "scroll timeout");
          } else {
            ScreenUtilities.logNavigation(scrollNode, "scroll finished");
          }

          refreshScrollNode();
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
