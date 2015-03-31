package org.nbp.b2g.ui;

import android.util.Log;
import android.graphics.Rect;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class Action {
  private final static String LOG_TAG = Action.class.getName();

  public abstract boolean performAction ();

  public final String getName () {
    return getClass().getName();
  }

  public boolean parseOperand (int keyMask, String operand) {
    Log.w(LOG_TAG, "invalid " + getName() + " operand: " + operand);
    return false;
  }

  protected static int getNavigationKeys () {
    return KeyEvents.getNavigationKeys();
  }

  protected static boolean isChord () {
    return (getNavigationKeys() & KeyMask.SPACE) != 0;
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final InputConnection getInputConnection () {
    InputService service = getInputService();
    if (service == null) return null;

    InputConnection connection = service.getCurrentInputConnection();
    if (connection == null) Log.w(LOG_TAG, "no input connection");
    return connection;
  }

  protected final ScreenMonitor getScreenMonitor () {
    return ScreenMonitor.getScreenMonitor();
  }

  protected void log (String message) {
    Log.v(LOG_TAG, message);
  }

  protected void log (AccessibilityNodeInfo node, String reason) {
    StringBuilder sb = new StringBuilder();

    sb.append(reason);
    sb.append(':');

    {
      CharSequence text = node.getText();

      if (text != null) {
        sb.append(" \"");
        sb.append(text);
        sb.append('"');
      }
    }

    {
      CharSequence description = node.getContentDescription();

      if (description != null) {
        sb.append(" (");
        sb.append(description);
        sb.append(')');
      }
    }

    sb.append(' ');
    sb.append(node.getClassName());

    {
      Rect bounds = new Rect();
      node.getBoundsInScreen(bounds);

      sb.append(' ');
      sb.append(bounds.toShortString());
    }

    if (node.isFocusable()) sb.append(" ifb");
    if (node.isFocused()) sb.append(" ifd");
    if (node.isAccessibilityFocused()) sb.append(" afd");
    if (node.isCheckable()) sb.append(" ckb");
    if (node.isChecked()) sb.append(" ckd");
    if (node.isSelected()) sb.append(" sld");
    if (node.isScrollable()) sb.append(" scb");

    log(sb.toString());
  }

  protected AccessibilityNodeInfo getRootNode () {
    return ScreenUtilities.getRootNode();
  }

  public AccessibilityNodeInfo getCurrentNode () {
    return ScreenUtilities.getCurrentNode();
  }

  protected boolean performNodeAction (AccessibilityNodeInfo node, int action) {
    node = AccessibilityNodeInfo.obtain(node);
    int actions = action;
    String description;

    switch (action) {
      case AccessibilityNodeInfo.ACTION_FOCUS:
        description = "set input focus";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_FOCUS:
        description = "clear input focus";
        actions |= AccessibilityNodeInfo.ACTION_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
        description = "set accessibility focus";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
        description = "clear accessibility focus";
        actions |= AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_SELECT:
        description = "select node";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_SELECTION;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_SELECTION:
        description = "deselect node";
        actions |= AccessibilityNodeInfo.ACTION_SELECT;
        break;

      case AccessibilityNodeInfo.ACTION_SCROLL_FORWARD:
        description = "scroll forward";
        break;

      case AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD:
        description = "scroll backward";
        break;

      case AccessibilityNodeInfo.ACTION_CLICK:
        description = "click node";
        break;

      case AccessibilityNodeInfo.ACTION_LONG_CLICK:
        description = "long click node";
        break;

      default:
        description = "node action" + action;
        break;
    }

    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) log(node, description);

    while (node != null) {
      if ((node.getActions() & actions) != 0) {
        boolean done = false;

        switch (action) {
          case AccessibilityNodeInfo.ACTION_FOCUS:
            if (node.isFocused()) done = true;
            break;

          case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
            if (node.isAccessibilityFocused()) done = true;
            break;

          case AccessibilityNodeInfo.ACTION_SELECT:
            if (node.isSelected()) done = true;
            break;

          default:
            break;
        }

        if (done) {
          description += " already";
        } else if (node.performAction(action)) {
          done = true;
          description += " succeeded";
        } else {
          description += " failed";
        }

        if (ApplicationParameters.LOG_PERFORMED_ACTIONS) log(node, description);
        return done;
      }

      AccessibilityNodeInfo parent = node.getParent();
      node.recycle();
      node = parent;
    }

    description += " failed";
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) log(description);
    return false;
  }

  protected Action () {
  }
}
