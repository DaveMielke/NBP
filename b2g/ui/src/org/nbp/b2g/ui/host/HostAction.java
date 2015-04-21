package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

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

  protected void log (AccessibilityNodeInfo node, String reason) {
    log((reason + ": " + ScreenUtilities.toString(node)));
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

    if (ApplicationParameters.CURRENT_LOG_ACTIONS) log(node, description);

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

        if (ApplicationParameters.CURRENT_LOG_ACTIONS) log(node, description);
        return done;
      }

      AccessibilityNodeInfo parent = node.getParent();
      node.recycle();
      node = parent;
    }

    description += " failed";
    if (ApplicationParameters.CURRENT_LOG_ACTIONS) log(description);
    return false;
  }

  protected HostAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
