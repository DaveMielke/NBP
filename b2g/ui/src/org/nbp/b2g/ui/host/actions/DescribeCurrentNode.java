package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.LinkedHashMap;

import android.view.accessibility.AccessibilityNodeInfo;

import android.widget.Switch;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.graphics.Point;
import android.graphics.Rect;

public class DescribeCurrentNode extends DescriptionAction {
  private class NodeDescriber {
    public NodeDescriber () {
    }

    protected final void appendLength (StringBuilder sb, AccessibilityNodeInfo node) {
      CharSequence text = node.getText();

      if (text != null) {
        sb.append(" Len:");
        sb.append(text.length());
      }
    }

    protected final void appendChecked (
      StringBuilder sb, AccessibilityNodeInfo node, String no, String yes
    ) {
      sb.append(' ');
      sb.append(node.isChecked()? yes: no);
    }

    public String getType (AccessibilityNodeInfo node) {
      return null;
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
            appendChecked(sb, node, "off", "on");
          }
        }
      );

      put(CheckBox.class,
        new NodeDescriber() {
          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            appendChecked(sb, node, "unchecked", "checked");
          }
        }
      );

      put(RadioButton.class,
        new NodeDescriber() {
          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            appendChecked(sb, node, "unselected", "selected");
          }
        }
      );

      put(Button.class,
        new NodeDescriber() {
        }
      );

      put(EditText.class,
        new NodeDescriber() {
          @Override
          public String getType (AccessibilityNodeInfo node) {
            return node.isPassword()? "password": "edit";
          }

          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            appendLength(sb, node);
          }
        }
      );

      put(TextView.class,
        new NodeDescriber() {
          @Override
          public String getType (AccessibilityNodeInfo node) {
            return "text";
          }

          @Override
          public void describeNode (StringBuilder sb, AccessibilityNodeInfo node) {
            appendLength(sb, node);
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

  private final static int toPercentage (int reference, int value) {
    return (value * 100) / reference;
  }

  @Override
  public void makeDescription (StringBuilder sb) {
    AccessibilityNodeInfo node = Endpoints.host.get().getCurrentNode();
    if (node == null) return;

    String typeName = node.getClassName().toString();
    NodeDescriber nodeDescriber = getNodeDescriber(typeName);

    if (nodeDescriber != null) {
      String type = nodeDescriber.getType(node);
      if (type != null) appendString(sb, type);
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

    if (nodeDescriber != null) {
      int length = sb.length();
      nodeDescriber.describeNode(sb, node);
      if (sb.length() > length) sb.insert(length, ':');
    }

    {
      Point size = ApplicationContext.getScreenSize();

      if (size != null) {
        Rect region = new Rect();
        node.getBoundsInScreen(region);

        sb.append('\n');
        sb.append("Locn:");

        sb.append(' ');
        sb.append('[');
        sb.append(toPercentage(size.x, region.left));
        sb.append(',');
        sb.append(toPercentage(size.y, region.top));
        sb.append(']');

        sb.append(' ');
        sb.append(toPercentage(size.x, (region.right - region.left)));
        sb.append('x');
        sb.append(toPercentage(size.y, (region.bottom - region.top)));
      }
    }
  }

  public DescribeCurrentNode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
