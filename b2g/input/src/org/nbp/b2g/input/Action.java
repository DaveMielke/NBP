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
    return monitor.getRootInActiveWindow();
  }

  public AccessibilityNodeInfo getCurrentNode () {
    AccessibilityNodeInfo root = getRootNode();
    if (root == null) return null;

    AccessibilityNodeInfo current = root.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
    if (current == null) current = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);

    root.recycle();
    return current;
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
