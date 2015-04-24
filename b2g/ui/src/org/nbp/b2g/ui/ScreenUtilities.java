package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Build;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class ScreenUtilities {
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

    return sb.toString();
  }

  public static void logNavigation (AccessibilityNodeInfo node, String reason) {
    if (ApplicationParameters.CURRENT_LOG_NAVIGATION) {
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

  private static AccessibilityNodeInfo findSelectedNode (AccessibilityNodeInfo root) {
    root = AccessibilityNodeInfo.obtain(root);
    if (root.isSelected()) return root;

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

    root.recycle();
    return node;
  }

  private static AccessibilityNodeInfo findTextNode (AccessibilityNodeInfo root) {
    root = AccessibilityNodeInfo.obtain(root);
    if (root.getText() != null) return root;

    AccessibilityNodeInfo node = null;
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        node = findTextNode(child);
        child.recycle();
        if (node != null) break;
      }
    }

    root.recycle();
    return node;
  }

  private static AccessibilityNodeInfo findCurrentNode (AccessibilityNodeInfo root) {
    AccessibilityNodeInfo node;
    logNavigation(root, "finding current node");

    if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)) == null) {
      if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)) == null) {
        node = AccessibilityNodeInfo.obtain(root);
        logNavigation(node, "using root node");
      } else {
        logNavigation(node, "found input focus");
      }
    } else {
      logNavigation(node, "found accessibility focus");
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
      AccessibilityNodeInfo text = findTextNode(node);

      if (text != null) {
        node.recycle();
        node = text;
        logNavigation(node, "found text node");
      }
    }

    if (!node.isAccessibilityFocused()) {
      if (!node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
        logNavigation(node, "accessibility focus not set");
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

  public static boolean isEditable (AccessibilityNodeInfo node) {
    if (node == null) return false;

    if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
      return node.isEditable();
    }

    return canAssign(android.widget.EditText.class, node);
  }

  public static boolean isSeekable (AccessibilityNodeInfo node) {
    if (node == null) return false;
    if (node.getChildCount() > 0) return false;

    int actions = node.getActions();
    if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) != 0) return true;
    if ((actions & AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) != 0) return true;
    return false;
  }

  public static boolean hasAction (AccessibilityNodeInfo node, int action) {
    if (node == null) return false;
    return (node.getActions() & action) != 0;
  }

  public static String getClassName (AccessibilityNodeInfo node) {
    return LanguageUtilities.getClassName(node.getClassName());
  }

  private ScreenUtilities () {
  }
}
