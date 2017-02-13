package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import java.util.List;
import java.util.ArrayList;

import org.nbp.common.Logger;

import android.view.accessibility.AccessibilityNodeInfo;

public class NodeIterator implements Logger.Iterator {
  private final List<String> logContent = new ArrayList<String>();
  private int logIndex = 0;

  private final void logTree (AccessibilityNodeInfo node, String name) {
    if (node != null) {
      logContent.add((name + " " + ScreenUtilities.toString(node)));

      int childCount = node.getChildCount();
      for (int childIndex=0; childIndex<childCount; childIndex+=1) {
        AccessibilityNodeInfo child = node.getChild(childIndex);

        if (child != null) {
          logTree(child, (name + "." + childIndex));
          child.recycle();
        }
      }
    }
  }

  public NodeIterator (AccessibilityNodeInfo root) {
    logTree(root, "root");
  }

  public NodeIterator () {
    AccessibilityNodeInfo root = ScreenUtilities.getRootNode();

    if (root != null) {
      logTree(root, "root");
      root.recycle();
      root = null;
    }
  }

  @Override
  public final String getText () {
    return logContent.get(logIndex-1);
  }

  @Override
  public final boolean next () {
    if (logIndex == logContent.size()) return false;
    logIndex += 1;
    return true;
  }
}
