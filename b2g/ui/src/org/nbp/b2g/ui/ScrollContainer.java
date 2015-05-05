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

  private boolean refreshNode () {
    synchronized (this) {
      AccessibilityNodeInfo node = ScreenUtilities.getRefreshedNode(scrollNode);
      if (node == null) return false;

      synchronized (scrollContainers) {
        scrollContainers.remove(scrollNode);
        scrollNode.recycle();

        scrollNode = node;
        scrollContainers.put(node, this);
      }
    }

    return true;
  }

  private boolean scrollTimeout = false;

  public boolean scroll (ScrollDirection direction) {
    boolean scrolled = false;

    synchronized (this) {
      deselectDescendants();
      scrollTimeout = true;

      if (scrollNode.performAction(direction.getNodeAction())) {
        ScreenUtilities.logNavigation(scrollNode, "scroll started");

        try {
          wait(ApplicationParameters.VIEW_SCROLL_TIMEOUT);
          scrolled = true;

          if (scrollTimeout) {
            ScreenUtilities.logNavigation(scrollNode, "scroll timeout");
          } else {
            ScreenUtilities.logNavigation(scrollNode, "scroll finished");
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
