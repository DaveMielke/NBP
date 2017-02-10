package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;

public abstract class ScreenLogger {
  public abstract boolean logScreen ();
  protected abstract boolean write (String text);

  private final boolean writeTree (AccessibilityNodeInfo node, String name) {
    if (node != null) {
      if (!write((name + " " + ScreenUtilities.toString(node)))) {
        return false;
      }

      int childCount = node.getChildCount();
      for (int childIndex=0; childIndex<childCount; childIndex+=1) {
        AccessibilityNodeInfo child = node.getChild(childIndex);

        if (child != null) {
          boolean ok = writeTree(child, (name + "." + childIndex));
          child.recycle();
          if (!ok) return false;
        }
      }
    }

    return true;
  }

  protected final boolean writeScreen () {
    AccessibilityNodeInfo root = ScreenUtilities.getRootNode();
    if (root == null) return true;

    boolean ok = writeTree(root, "root");
    root.recycle();
    return ok;
  }

  protected ScreenLogger () {
  }
}
