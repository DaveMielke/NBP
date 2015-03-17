package org.nbp.b2g.input;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public final class NodeAction extends ScreenAction {
  private static final String LOG_TAG = NodeAction.class.getName();

  protected final int nodeAction;

  @Override
  public final boolean performAction () {
    AccessibilityNodeInfo node = getCurrentNode();
    if (node == null) return false;
    return performNodeAction(node, nodeAction);
  }

  public NodeAction (int nodeAction, String name) {
    super("NODE_" + name);
    this.nodeAction = nodeAction;
  }

  public static void add (int keyMask, int nodeAction, String name) {
    add(keyMask, new NodeAction(nodeAction, name));
  }
}
