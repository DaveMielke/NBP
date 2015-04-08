package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class NodeAction extends HostAction {
  public final static int NULL_NODE_ACTION = 0;

  protected int getNodeAction () {
    return NULL_NODE_ACTION;
  }

  @Override
  public boolean performAction () {
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

  protected NodeAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
