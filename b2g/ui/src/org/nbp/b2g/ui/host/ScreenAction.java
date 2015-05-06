package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScreenAction extends HostAction {
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
    if (node.isCheckable()) return true;
    if (ScreenUtilities.isSeekable(node)) return true;
    return false;
  }

  protected boolean canSetAsCurrent (AccessibilityNodeInfo node) {
    return !ScreenUtilities.canAssign(android.widget.ListView.class, node);
  }

  protected boolean setCurrentNode (AccessibilityNodeInfo node, boolean force) {
    ScreenUtilities.logNavigation(node, "setting node");

    if (!force) {
      if (node.getText() != null) {
        ScreenUtilities.logNavigation("node has text");
      } else if (node.getContentDescription() != null) {
        ScreenUtilities.logNavigation("node has description");
      } else {
        ScreenUtilities.logNavigation("node has no text");

        if (isActionable(node)) {
          ScreenUtilities.logNavigation("node is actionable");
        } else {
          ScreenUtilities.logNavigation("node is not actionable");
          return false;
        }
      }

      if (node.isEnabled()) {
        ScreenUtilities.logNavigation("node is enabled");
      } else {
        ScreenUtilities.logNavigation("node is disabled");
        return false;
      }

      if (canSetAsCurrent(node)) {
        ScreenUtilities.logNavigation("node is eligible");
      } else {
        ScreenUtilities.logNavigation("node is ineligible");
        return false;
      }

      if (node.isVisibleToUser()) {
        ScreenUtilities.logNavigation("node is visible");
      } else {
        ScreenUtilities.logNavigation("node is invisible");
        return false;
      }
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)) {
      ScreenUtilities.logNavigation("set accessibility focus succeeded");
    } else {
      ScreenUtilities.logNavigation("set accessibility focus failed");

      if (performNodeAction(node, AccessibilityNodeInfo.ACTION_FOCUS)) {
        ScreenUtilities.logNavigation("set input focus succeeded");
      } else {
        ScreenUtilities.logNavigation("set input focus failed");
        return false;
      }
    }

    if (performNodeAction(node, AccessibilityNodeInfo.ACTION_SELECT)) {
      ScreenUtilities.logNavigation("select node succeeded");
    } else {
      ScreenUtilities.logNavigation("select node failed");
    }

    getHostEndpoint().write(node, force, 0);
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

  protected ScreenAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
