package org.nbp.b2g.input;

import android.view.accessibility.AccessibilityNodeInfo;

public class NodeAction extends KeyCodeAction {
  public static final int NULL_NODE_ACTION = 0;

  protected int getNodeAction () {
    return NULL_NODE_ACTION;
  }

  @Override
  public final boolean performAction () {
    int nodeAction = getNodeAction();

    if (nodeAction != NULL_NODE_ACTION) {
      AccessibilityNodeInfo node = getCurrentNode();

      if (node != null) {
        if (performNodeAction(node, nodeAction)) {
          return true;
        }
      }
    }

    return false;
  }

  public NodeAction () {
    super();
  }
}
