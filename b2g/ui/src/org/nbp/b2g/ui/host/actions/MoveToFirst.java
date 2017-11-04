package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public class MoveToFirst extends MoveForward {
  @Override
  public boolean performAction () {
    boolean found = false;
    AccessibilityNodeInfo root = ScreenUtilities.getRootNode();

    if (root != null) {
      if (setCurrentNode(root)) {
        found = true;
      } else if (moveToNextNode(root, true)) {
        found = true;
      }

      root.recycle();
    }

    return found;
  }

  public MoveToFirst (Endpoint endpoint) {
    super(endpoint);
  }
}
