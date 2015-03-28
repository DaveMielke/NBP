package org.nbp.b2g.input;

import android.util.Log;

import android.inputmethodservice.InputMethodService;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class Action {
  private static final String LOG_TAG = Action.class.getName();

  public abstract boolean performAction ();

  public final String getName () {
    return getClass().getName();
  }

  public boolean parseOperand (int keyMask, String operand) {
    Log.w(LOG_TAG, "invalid " + getName() + " operand: " + operand);
    return false;
  }

  protected static int getKeyMask () {
    return KeyEvents.getKeyMask();
  }

  protected static boolean isChord () {
    return (getKeyMask() & KeyMask.SPACE) != 0;
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final ScreenMonitor getScreenMonitor () {
    return ScreenMonitor.getScreenMonitor();
  }

  protected AccessibilityNodeInfo getRootNode () {
    ScreenMonitor monitor = getScreenMonitor();
    if (monitor == null) return null;

    AccessibilityNodeInfo root = monitor.getRootInActiveWindow();
    if (root == null) Log.w(LOG_TAG, "no root node");
    return root;
  }

  public AccessibilityNodeInfo getCurrentNode () {
    AccessibilityNodeInfo root = getRootNode();
    if (root == null) return null;

    AccessibilityNodeInfo node;
    if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)) == null) {
      if ((node = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)) == null) {
        Log.w(LOG_TAG, "no current node");
      }
    }

    root.recycle();
    return node;
  }

  protected boolean performNodeAction (AccessibilityNodeInfo node, int action) {
    node = AccessibilityNodeInfo.obtain(node);

    while (node != null) {
      switch (action) {
        case AccessibilityNodeInfo.ACTION_FOCUS:
          if (node.isFocused()) return true;
          break;

        case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
          if (node.isAccessibilityFocused()) return true;
          break;

        case AccessibilityNodeInfo.ACTION_SELECT:
          if (node.isSelected()) return true;
          break;

        default:
          break;
      }

      if ((node.getActions() & action) != 0) {
        if (node.performAction(action)) {
          return true;
        }
      }

      AccessibilityNodeInfo parent = node.getParent();
      node.recycle();
      node = parent;
    }

    return false;
  }

  protected Action () {
  }
}
