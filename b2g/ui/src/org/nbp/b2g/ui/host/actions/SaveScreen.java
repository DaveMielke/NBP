package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public class SaveScreen extends ActivityAction {
  private final static String LOG_TAG = SaveScreen.class.getName();

  private void write (String string) {
    Log.v(LOG_TAG, string);
  }

  private void write (AccessibilityNodeInfo node, String name) {
    if (node != null) {
      Log.v(LOG_TAG, "screen node: " + name + " " + ScreenUtilities.toString(node));

      int childCount = node.getChildCount();
      for (int childIndex=0; childIndex<childCount; childIndex+=1) {
        AccessibilityNodeInfo child = node.getChild(childIndex);

        if (child != null) {
          write(child, (name + "." + childIndex));
          child.recycle();
        }
      }
    }
  }

  @Override
  public boolean performAction () {
    write("begin screen elements");
    AccessibilityNodeInfo root = ScreenUtilities.getRootNode();

    if (root != null) {
      write(root, "root");
      root.recycle();
    }

    write("end screen elements");
    return true;
  }

  public SaveScreen (Endpoint endpoint) {
    super(endpoint, true);
  }
}
