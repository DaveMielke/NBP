package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveUp extends ScreenAction {
  @Override
  public final boolean performAction () {
    boolean moved = false;
    AccessibilityNodeInfo node = getCurrentNode();

    if (node != null) {
      AccessibilityNodeInfo parent = node.getParent();

      if (parent != null) {
        if (setCurrentNode(parent, true)) moved = true;
        parent.recycle();
      }

      node.recycle();
    }

    return true;
  }

  public MoveUp () {
    super();
  }
}
