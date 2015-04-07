package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Build;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class ScreenUtilities {
  private final static String LOG_TAG = ScreenUtilities.class.getName();

  public static boolean canAssign (Class to, AccessibilityNodeInfo from) {
    return LanguageUtilities.canAssign(to, from.getClassName());
  }

  public static AccessibilityNodeInfo getRootNode () {
    ScreenMonitor monitor = ScreenMonitor.getScreenMonitor();
    if (monitor == null) return null;

    AccessibilityNodeInfo root = monitor.getRootInActiveWindow();
    if (root == null) Log.w(LOG_TAG, "no root node");
    return root;
  }

  private static AccessibilityNodeInfo establishCurrentNode (AccessibilityNodeInfo root) {
    root = AccessibilityNodeInfo.obtain(root);

    if (root.getText() != null) {
      if (root.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
        return root;
      }
    }

    AccessibilityNodeInfo node = null;
    int childCount = root.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = root.getChild(childIndex);

      if (child != null) {
        node = establishCurrentNode(child);
        child.recycle();
        if (node != null) break;
      }
    }

    root.recycle();
    return node;
  }

  public static AccessibilityNodeInfo getCurrentNode () {
    AccessibilityNodeInfo root = getRootNode();
    if (root == null) return null;

    AccessibilityNodeInfo node;
    if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)) == null) {
      if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)) == null) {
        if ((node = establishCurrentNode(root)) == null) {
          Log.w(LOG_TAG, "no current node");
        }
      }
    }

    root.recycle();
    return node;
  }

  public static boolean isEditable (AccessibilityNodeInfo node) {
    if (node == null) return false;

    if (ApplicationUtilities.haveSdkVersion(Build.VERSION_CODES.JELLY_BEAN_MR2)) {
      return node.isEditable();
    }

    return canAssign(android.widget.EditText.class, node);
  }

  public static String getClassName (AccessibilityNodeInfo node) {
    String name = node.getClassName().toString();
    int index = name.lastIndexOf('.');
    return name.substring(index+1);
  }

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
      Rect bounds = new Rect();
      node.getBoundsInScreen(bounds);

      sb.append(' ');
      sb.append(bounds.toShortString());
    }

    return sb.toString();
  }

  private ScreenUtilities () {
  }
}
