package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Bundle;

public abstract class NodeAction extends HostAction {
  public final static int NULL_NODE_ACTION = 0;

  protected int getNodeAction () {
    return NULL_NODE_ACTION;
  }

  protected Bundle getNodeArguments () {
    return null;
  }

  @Override
  public boolean performAction () {
    int action = getNodeAction();

    if (action != NULL_NODE_ACTION) {
      AccessibilityNodeInfo node = getCurrentNode();

      if (node != null) {
        if (performNodeAction(node, action, getNodeArguments())) {
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
