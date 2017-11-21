package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import android.view.accessibility.AccessibilityNodeInfo;

import android.view.View;

public class DescribeCurrentNode extends DescriptionAction {
  private class NodeDescriber {
    public NodeDescriber () {
    }

    public int getType (AccessibilityNodeInfo node) {
      return 0;
    }

    public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
    }
  }

  private final static Map<Class<? extends View>, NodeDescriber> nodeDescribers =
         new LinkedHashMap<Class<? extends View>, NodeDescriber>();

  @Override
  public void makeDescription (StringBuilder sb) {
    AccessibilityNodeInfo node = Endpoints.host.get().getCurrentNode();
    if (node == null) return;

    {
      String name = node.getClassName().toString();

      {
        int index = name.lastIndexOf('.');
        if (index >= 0) name = name.substring(index+1);
      }

      appendString(sb, toWords(name));
    }
  }

  public DescribeCurrentNode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
