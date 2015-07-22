package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Build;

import android.graphics.Point;
import android.graphics.Rect;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScreenUtilities {
  private final static String LOG_TAG = ScreenUtilities.class.getName();

  public static String toString (AccessibilityNodeInfo node) {
    StringBuilder sb = new StringBuilder();
    CharSequence characters;

    sb.append(getClassName(node));

    if ((characters = node.getText()) != null) {
      sb.append(" \"");
      sb.append(characters);
      sb.append('"');
    }

    if ((characters = node.getContentDescription()) != null) {
      sb.append(" (");
      sb.append(characters);
      sb.append(')');
    }

    {
      AccessibilityNodeInfo parent = node.getParent();

      if (parent != null) {
        parent.recycle();
      } else {
        sb.append(" root");
      }
    }

    {
      int count = node.getChildCount();

      if (count != 0) {
        sb.append(" cld=");
        sb.append(count);
      }
    }

    if (!node.isEnabled()) sb.append(" dsb");
    if (!node.isVisibleToUser()) sb.append(" inv");

    if (isEditable(node)) sb.append(" edt");
    if (node.isPassword()) sb.append(" pwd");

    if (node.isFocusable()) sb.append(" ifb");
    if (node.isFocused()) sb.append(" ifd");
    if (node.isAccessibilityFocused()) sb.append(" afd");

    if (node.isScrollable()) sb.append(" scb");
    if (node.isCheckable()) sb.append(" ckb");
    if (node.isChecked()) sb.append(" ckd");
    if (node.isSelected()) sb.append(" sld");

    {
      int actions = node.getActions();

      if ((actions & AccessibilityNodeInfo.ACTION_FOCUS) != 0) sb.append(" ifs");
      if ((actions & AccessibilityNodeInfo.ACTION_CLEAR_FOCUS) != 0) sb.append(" ifc");

      if ((actions & AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS) != 0) sb.append(" afs");
      if ((actions & AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0) sb.append(" afc");

      if ((actions & AccessibilityNodeInfo.ACTION_SELECT) != 0) sb.append(" sls");
      if ((actions & AccessibilityNodeInfo.ACTION_CLEAR_SELECTION) != 0) sb.append(" slc");

      if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) != 0) sb.append(" scn");
      if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) != 0) sb.append(" scp");

      if ((actions & AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY) != 0) sb.append(" mvn");
      if ((actions & AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY) != 0) sb.append(" mvp");

      if ((actions & AccessibilityNodeInfo.ACTION_CLICK) != 0) sb.append(" clk");
      if ((actions & AccessibilityNodeInfo.ACTION_LONG_CLICK) != 0) sb.append(" lng");
    }

    {
      int granularities = node.getMovementGranularities();
      if ((granularities & AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER) != 0) sb.append(" mgc");
      if ((granularities & AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD) != 0) sb.append(" mgw");
      if ((granularities & AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH) != 0) sb.append(" mgp");
      if ((granularities & AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE) != 0) sb.append(" mgl");
      if ((granularities & AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE) != 0) sb.append(" mgs");
    }

    {
      Rect bounds = new Rect();
      node.getBoundsInScreen(bounds);

      sb.append(' ');
      sb.append(bounds.toShortString());
    }

    sb.append(" #");
    sb.append(node.hashCode());

    return sb.toString();
  }

  public static void logNavigation (AccessibilityNodeInfo node, String reason) {
    if (ApplicationSettings.LOG_NAVIGATION) {
      if (node != null) reason += ": " + toString(node);
      Log.v(LOG_TAG, reason);
    }
  }

  public static void logNavigation (String reason) {
    logNavigation(null, reason);
  }

  public static boolean canAssign (Class to, AccessibilityNodeInfo from) {
    return LanguageUtilities.canAssign(to, from.getClassName());
  }

  public static boolean hasAction (AccessibilityNodeInfo node, int action) {
    if (node == null) return false;
    return (node.getActions() & action) != 0;
  }

  public static boolean isEditable (AccessibilityNodeInfo node) {
    if (node == null) return false;

    if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
      return node.isEditable();
    }

    return canAssign(android.widget.EditText.class, node);
  }

  public static boolean isBar (AccessibilityNodeInfo node) {
    if (canAssign(android.widget.ProgressBar.class, node)) return true;
    return false;
  }

  public static boolean isSlider (AccessibilityNodeInfo node) {
    if (canAssign(android.widget.AbsSeekBar.class, node)) return true;

    if (node == null) return false;
    if (node.getChildCount() > 0) return false;

    int actions = node.getActions();
    if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) != 0) return true;
    if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) != 0) return true;
    return false;
  }

  public static AccessibilityNodeInfo getRefreshedNode (AccessibilityNodeInfo node) {
    if (node != null) {
      if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
        node = AccessibilityNodeInfo.obtain(node);

        if (!node.refresh()) {
          node.recycle();
          node = null;
        }

        return node;
      }

      {
        int childCount = node.getChildCount();

        for (int childIndex=0; childIndex<childCount; childIndex+=1) {
          AccessibilityNodeInfo child = node.getChild(childIndex);

          if (child != null) {
            AccessibilityNodeInfo parent = child.getParent();

            child.recycle();
            child = null;

            if (node.equals(parent)) {
              return parent;
            }

            if (parent != null) {
              parent.recycle();
              parent = null;
            }
          }
        }
      }

      {
        AccessibilityNodeInfo parent = node.getParent();

        if (parent != null) {
          int childCount = parent.getChildCount();

          for (int childIndex=0; childIndex<childCount; childIndex+=1) {
            AccessibilityNodeInfo child = parent.getChild(childIndex);

            if (node.equals(child)) {
              parent.recycle();
              parent = null;
              return child;
            }

            if (child != null) {
              child.recycle();
              child = null;
            }
          }

          parent.recycle();
          parent = null;
        }
      }
    }

    return null;
  }

  public static AccessibilityNodeInfo getRootNode (AccessibilityNodeInfo node) {
    if (node == null) return null;
    node = AccessibilityNodeInfo.obtain(node);

    while (true) {
      AccessibilityNodeInfo parent = node.getParent();
      if (parent == null) return node;

      node.recycle();
      node = parent;
    }
  }

  public static AccessibilityNodeInfo getRootNode () {
    ScreenMonitor monitor = ScreenMonitor.getScreenMonitor();
    if (monitor == null) return null;

    AccessibilityNodeInfo root = monitor.getRootInActiveWindow();
    if (root == null) Log.w(LOG_TAG, "no root node");
    return root;
  }

  public static AccessibilityNodeInfo findSelectedNode (AccessibilityNodeInfo root) {
    root = AccessibilityNodeInfo.obtain(root);
    AccessibilityNodeInfo node = null;
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        node = findSelectedNode(child);
        child.recycle();
        if (node != null) break;
      }
    }

    if (node == null) {
      if (root.isSelected()) {
        return root;
      }
    }

    root.recycle();
    return node;
  }

  public static boolean isContainer (AccessibilityNodeInfo node) {
    if (canAssign(android.view.ViewGroup.class, node)) return true;
    if (node.getChildCount() > 0) return true;
    return false;
  }

  public static boolean isSignificant (AccessibilityNodeInfo node) {
    if (node.getText() != null) {
      logNavigation(node, "node has text");
    } else if (isEditable(node)) {
      logNavigation(node, "node is editable");
    } else if (node.getContentDescription() != null) {
      logNavigation(node, "node has description");
    } else if (node.isCheckable()) {
      logNavigation(node, "node is checkable");
    } else if (isBar(node)) {
      logNavigation(node, "node is bar");
    } else if (node.isFocusable() && !isContainer(node)) {
      logNavigation(node, "node is input focusable");
    } else {
      logNavigation(node, "node is not significant");
      return false;
    }

    if (!node.isVisibleToUser()) {
      logNavigation(node, "node is invisible");
      return false;
    }

    if (!node.isEnabled()) {
      logNavigation(node, "node is disabled");
    }

    return true;
  }

  public static AccessibilityNodeInfo findSignificantNode (AccessibilityNodeInfo root) {
    root = AccessibilityNodeInfo.obtain(root);
    if (isSignificant(root)) return root;

    AccessibilityNodeInfo node = null;
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        node = findSignificantNode(child);
        child.recycle();
        if (node != null) break;
      }
    }

    root.recycle();
    return node;
  }

  public static AccessibilityNodeInfo findCurrentNode (AccessibilityNodeInfo root) {
    AccessibilityNodeInfo node;
    logNavigation(root, "finding current node");

    if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)) != null) {
      logNavigation(node, "found accessibility focus");
    } else {
      if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)) != null) {
        logNavigation(node, "found input focus");
      } else {
        node = AccessibilityNodeInfo.obtain(root);
        logNavigation(node, "using root node");
      }

      {
        AccessibilityNodeInfo selected = findSelectedNode(node);

        if (selected != null) {
          node.recycle();
          node = selected;
          logNavigation(node, "found selected node");
        }
      }

      {
        AccessibilityNodeInfo significant = findSignificantNode(node);

        if (significant != null) {
          node.recycle();
          node = significant;
          logNavigation(node, "found significant node");
        }
      }
    }

    logNavigation(node, "found current node");
    return node;
  }

  public static AccessibilityNodeInfo getCurrentNode () {
    AccessibilityNodeInfo root = getRootNode();
    if (root == null) return null;

    AccessibilityNodeInfo current = findCurrentNode(root);
    root.recycle();
    return current;
  }

  public static AccessibilityNodeInfo getCurrentNode (AccessibilityNodeInfo node) {
    AccessibilityNodeInfo root = getRootNode(node);
    if (root == null) return null;

    AccessibilityNodeInfo current = findCurrentNode(root);
    root.recycle();
    return current;
  }

  public static void deselectTree (AccessibilityNodeInfo root) {
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        deselectTree(child);
        child.recycle();
      }
    }

    if (root.isSelected()) {
      if (root.performAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION)) {
        logNavigation(root, "deselect succeeded");
      } else {
        logNavigation(root, "deselect failed");
      }
    }
  }

  public static void deselectSiblings (AccessibilityNodeInfo parent, AccessibilityNodeInfo node) {
    int childCount = parent.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = parent.getChild(childIndex);

      if (child != null) {
        if (!child.equals(node)) deselectTree(child);
        child.recycle();
      }
    }
  }

  public static void deselectChildren (AccessibilityNodeInfo parent) {
    deselectSiblings(parent, null);
  }

  public static void isolateSelection (AccessibilityNodeInfo node) {
    node = AccessibilityNodeInfo.obtain(node);

    while (!node.isFocused()) {
      if (node.isSelected()) {
        logNavigation(node, "select unnecessary");
      } else if (node.performAction(AccessibilityNodeInfo.ACTION_SELECT)) {
        logNavigation(node, "select succeeded");
      } else {
        logNavigation(node, "select failed");
      }

      if (node.isFocusable()) {
        if (node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)) {
          logNavigation(node, "set input focus succeeded");
          break;
        }

        logNavigation(node, "set input focus failed");
      }

      AccessibilityNodeInfo parent = node.getParent();
      if (parent == null) break;

      deselectSiblings(parent, node);
      node.recycle();
      node = parent;
    }

    node.recycle();
  }

  public static boolean setCurrentNode (AccessibilityNodeInfo node) {
    logNavigation(node, "setting current node");

    if (node.isAccessibilityFocused()) {
      logNavigation(node, "set accessibility focus unnecessary");
    } else if (node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
      logNavigation(node, "set accessibility focus succeeded");
    } else {
      logNavigation(node, "set accessibility focus failed");
    }

    isolateSelection(node);
    return true;
  }

  public static void selectChild (AccessibilityNodeInfo root, int childIndex) {
    int childCount = root.getChildCount();

    if ((childIndex >= 0) && (childIndex < childCount)) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        AccessibilityNodeInfo node = findCurrentNode(child);

        if (node != null) {
          setCurrentNode(node);
          node.recycle();
        }

        child.recycle();
      }
    }
  }

  public static Rect getNodeRegion (AccessibilityNodeInfo node) {
    if (node == null) return null;

    Rect region = new Rect();
    node.getBoundsInScreen(region);
    return region;
  }

  public static Point getNodeCenter (AccessibilityNodeInfo node) {
    Rect region = getNodeRegion(node);
    if (region == null) return null;

    Point location = new Point(
      ((region.left + region.right) / 2),
      ((region.top + region.bottom) / 2)
    );

    return location;
  }

  public static String getClassName (AccessibilityNodeInfo node) {
    return LanguageUtilities.getClassName(node.getClassName());
  }

  private ScreenUtilities () {
  }
}
