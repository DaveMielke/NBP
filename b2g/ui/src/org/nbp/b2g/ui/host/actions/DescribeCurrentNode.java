package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import android.view.accessibility.AccessibilityNodeInfo;

import android.widget.Switch;
import android.widget.CheckBox;

public class DescribeCurrentNode extends DescriptionAction {
  private class NodeDescriber {
    public NodeDescriber () {
    }

    public int getType () {
      return 0;
    }

    public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
    }
  }

  private final Map<Class, NodeDescriber> nodeDescribers =
         new LinkedHashMap<Class, NodeDescriber>() {
    {
      put(Switch.class,
        new NodeDescriber() {
          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            sb.append(' ');
            sb.append(node.isChecked()? "on": "off");
          }
        }
      );

      put(CheckBox.class,
        new NodeDescriber() {
          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            sb.append(' ');
            sb.append(node.isChecked()? "checked": "unchecked");
          }
        }
      );
    }
  };

  private final NodeDescriber getNodeDescriber (String typeName) {
    try {
      Class nodeType = Class.forName(typeName);

      for (Class describerType : nodeDescribers.keySet()) {
        if (describerType.isAssignableFrom(nodeType)) {
          return nodeDescribers.get(describerType);
        }
      }
    } catch (ClassNotFoundException exception) {
    }

    return null;
  }

  @Override
  public void makeDescription (StringBuilder sb) {
    AccessibilityNodeInfo node = Endpoints.host.get().getCurrentNode();
    if (node == null) return;

    String typeName = node.getClassName().toString();
    NodeDescriber nodeDescriber = getNodeDescriber(typeName);

    if (nodeDescriber != null) {
      int resource = nodeDescriber.getType();
      if (resource != 0) appendString(sb, resource);
    }

    if (sb.length() == 0) {
      {
        int index = typeName.lastIndexOf('.');
        if (index >= 0) typeName = typeName.substring(index+1);
      }

      appendString(sb, toWords(typeName));
    }

    {
      CharSequence text = node.getContentDescription();

      if (text != null) {
        if (text.length() > 0) {
          sb.append(' ');
          sb.append('[');
          sb.append(text);
          sb.append(']');
        }
      }
    }

    sb.append(':');
    if (nodeDescriber != null) nodeDescriber.describeNode(sb, node);
  }

  public DescribeCurrentNode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
