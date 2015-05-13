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

  protected boolean setCurrentNode (AccessibilityNodeInfo node, boolean force) {
    ScreenUtilities.logNavigation(node, "setting node");

    if (!force) {
      if (!ScreenUtilities.isSignificant(node)) {
        return false;
      }
    }

    if (!ScreenUtilities.setCurrentNode(node)) return false;
    performNodeAction(node, AccessibilityNodeInfo.ACTION_FOCUS);

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
