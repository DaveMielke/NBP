package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToLast extends MoveBackward {
  @Override
  public boolean performAction () {
    boolean found = false;
    AccessibilityNodeInfo root = ScreenUtilities.getRootNode();

    if (root != null) {
      if (moveToNode(root)) found = true;
      root.recycle();
    }

    return found;
  }

  public MoveToLast (Endpoint endpoint) {
    super(endpoint);
  }
}
