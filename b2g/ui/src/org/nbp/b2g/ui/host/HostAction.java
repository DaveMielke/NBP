package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.os.Bundle;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class HostAction extends ScanCodeAction {
  private final static String LOG_TAG = HostAction.class.getName();

  protected final HostEndpoint getHostEndpoint () {
    return (HostEndpoint)getEndpoint();
  }

  protected final ScreenMonitor getScreenMonitor () {
    return ScreenMonitor.getScreenMonitor();
  }

  protected AccessibilityNodeInfo getRootNode () {
    return ScreenUtilities.getRootNode();
  }

  public AccessibilityNodeInfo getCurrentNode () {
    AccessibilityNodeInfo node = getHostEndpoint().getCurrentNode();
    if (node == null) node = ScreenUtilities.getCurrentNode();
    return node;
  }

  private static void logNodeAction (AccessibilityNodeInfo node, String action, String status) {
    if (ApplicationParameters.CURRENT_LOG_NAVIGATION) {
      Log.v(LOG_TAG, String.format(
        "node action %s: %s: %s",
        status, action, ScreenUtilities.toString(node)
      ));
    }
  }

  protected boolean performNodeAction (AccessibilityNodeInfo node, int action, Bundle arguments) {
    int actions = action;
    String name;

    switch (action) {
      case AccessibilityNodeInfo.ACTION_FOCUS:
        name = "set input focus";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_FOCUS:
        name = "clear input focus";
        actions |= AccessibilityNodeInfo.ACTION_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
        name = "set accessibility focus";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
        name = "clear accessibility focus";
        actions |= AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS;
        break;

      case AccessibilityNodeInfo.ACTION_SELECT:
        name = "select node";
        actions |= AccessibilityNodeInfo.ACTION_CLEAR_SELECTION;
        break;

      case AccessibilityNodeInfo.ACTION_CLEAR_SELECTION:
        name = "deselect node";
        actions |= AccessibilityNodeInfo.ACTION_SELECT;
        break;

      case AccessibilityNodeInfo.ACTION_SCROLL_FORWARD:
        name = "scroll forward";
        break;

      case AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD:
        name = "scroll backward";
        break;

      case AccessibilityNodeInfo.ACTION_CLICK:
        name = "click node";
        break;

      case AccessibilityNodeInfo.ACTION_LONG_CLICK:
        name = "long click node";
        break;

      default:
        name = "node action " + action;
        break;
    }

    logNodeAction(node, name, "starting");
    AccessibilityNodeInfo current = AccessibilityNodeInfo.obtain(node);

    while (current != null) {
      if ((current.getActions() & actions) != 0) {
        boolean done = false;

        switch (action) {
          case AccessibilityNodeInfo.ACTION_FOCUS:
            if (current.isFocused()) done = true;
            break;

          case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
            if (current.isAccessibilityFocused()) done = true;
            break;

          case AccessibilityNodeInfo.ACTION_SELECT:
            if (current.isSelected()) done = true;
            break;

          default:
            break;
        }

        if (done) {
          logNodeAction(current, name, "unnecessary");
        } else if (current.performAction(action, arguments)) {
          logNodeAction(current, name, "succeeded");
          done = true;
        } else {
          logNodeAction(current, name, "failed");
        }

        return done;
      }

      AccessibilityNodeInfo parent = current.getParent();
      current.recycle();
      current = parent;
    }

    logNodeAction(node, name, "unsupported");
    return false;
  }

  protected boolean performNodeAction (AccessibilityNodeInfo node, int action) {
    return performNodeAction(node, action, null);
  }

  protected HostAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
