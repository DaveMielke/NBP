package org.nbp.b2g.input;

import android.util.Log;
import android.graphics.Rect;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScreenAction extends Action {
  private static final String LOG_TAG = ScreenAction.class.getName();

  protected CharSequence getNodeText (AccessibilityNodeInfo node) {
    CharSequence text;

    if ((text = node.getText()) != null) return text;
    if ((text = node.getContentDescription()) != null) return text;
    return null;
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

  public AccessibilityNodeInfo findNode (AccessibilityNodeInfo node, AccessibilityNodeInfo root) {
    if (root != null) {
      if (root.equals(node)) return AccessibilityNodeInfo.obtain(root);
      int childCount = root.getChildCount();

      for (int childIndex=0; childIndex<childCount; childIndex+=1) {
        AccessibilityNodeInfo child = root.getChild(childIndex);
        AccessibilityNodeInfo found = findNode(node, child);
        if (child != null) child.recycle();
        if (found != null) return found;
      }
    }

    return null;
  }

  public AccessibilityNodeInfo findNode (AccessibilityNodeInfo node) {
    AccessibilityNodeInfo root = getRootNode();
    AccessibilityNodeInfo found = findNode(node, root);
    if (root != null) root.recycle();
    return found;
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

  protected boolean isActionable (AccessibilityNodeInfo node) {
    if (node.isClickable()) return true;
    if (node.isLongClickable()) return true;
    return false;
  }

  protected boolean hasInnerText (AccessibilityNodeInfo node) {
    int childCount = node.getChildCount();

    for (int childIndex=0; childIndex<childCount; childIndex+=1) {
      AccessibilityNodeInfo child = node.getChild(childIndex);

      if (child != null) {
        boolean found = false;

        if (child.getText() != null) {
          if (!isActionable(child)) {
            found = true;
          } else if (hasInnerText(child)) {
            found = true;
          }
        }

        child.recycle();
        if (found) return true;
      }
    }

    return false;
  }

  protected boolean canSetAsCurrent (AccessibilityNodeInfo node) {
    String name = node.getClassName().toString();
    if (name.equals("android.widget.ListView")) return false;
    return true;
  }

  protected boolean setCurrentNode (AccessibilityNodeInfo node) {
    if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "setting node");

    if (node.getText() != null) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node has text");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node has no text");

      if (isActionable(node)) {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is actionable");

        if (hasInnerText(node)) {
          if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node has inner text");
          return false;
        } else {
          if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node has no inner text");
        }
      } else {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is not actionable");
        return false;
      }
    }

    if (node.isEnabled()) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is enabled");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is disabled");
      return false;
    }

    if (canSetAsCurrent(node)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is eligible");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is ineligible");
      return false;
    }

    if (node.isVisibleToUser()) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is visible");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("node is invisible");
      return false;
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_FOCUS)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set input focus succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set input focus failed");
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_SELECT)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("select node succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("select node failed");
    }

    if (node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set accessibility focus succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set accessibility focus failed");
      return false;
    }

    return true;
  }

  protected int findChildIndex (AccessibilityNodeInfo parent, AccessibilityNodeInfo node) {
    int count = parent.getChildCount();

    for (int index=0; index<count; index+=1) {
      AccessibilityNodeInfo child = parent.getChild(index);

      if (child != null) {
        boolean found = child.equals(node);
        child.recycle();
        if (found) return index;
      }
    }

    return -1;
  }

  protected ScreenAction (String name) {
    super(name);
  }
}
