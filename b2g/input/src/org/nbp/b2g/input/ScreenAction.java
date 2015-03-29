package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScreenAction extends Action {
  private final static String LOG_TAG = ScreenAction.class.getName();

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

  protected boolean setCurrentNode (AccessibilityNodeInfo node, boolean force) {
    if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log(node, "setting node");

    if (!force) {
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
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set accessibility focus succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set accessibility focus failed");

      if (performNodeAction(node, AccessibilityNodeInfo.ACTION_FOCUS)) {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set input focus succeeded");
      } else {
        if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("set input focus failed");
        return false;
      }
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_SELECT)) {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("select node succeeded");
    } else {
      if (ApplicationParameters.LOG_SCREEN_NAVIGATION) log("select node failed");
    }

    BrailleDevice.write(node, force);
    return true;
  }

  protected boolean setCurrentNode (AccessibilityNodeInfo node) {
    return setCurrentNode(node, false);
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

  protected ScreenAction () {
    super();
  }
}
