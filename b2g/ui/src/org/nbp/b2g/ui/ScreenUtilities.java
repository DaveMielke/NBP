package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Build;

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

  private ScreenUtilities () {
  }
}
