package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputConnection;

public class HostEndpoint extends Endpoint {
  private final static String LOG_TAG = HostEndpoint.class.getName();

  private AccessibilityNodeInfo currentNode = null;
  private boolean currentDescribe = false;

  private void resetNode () {
    if (currentNode != null) {
      currentNode.recycle();
      currentNode = null;
    }

    currentDescribe = false;
  }

  public boolean write (AccessibilityNodeInfo node, boolean describe, int indent) {
    String text;

    if (describe) {
      text = ScreenUtilities.toString(node);
    } else {
      StringBuilder sb = new StringBuilder();
      CharSequence characters;

      if (node.isCheckable()) {
        sb.append('[');
        sb.append(node.isChecked()? 'X': ' ');
        sb.append("] ");
      }

      if ((characters = node.getText()) != null) {
        sb.append(characters);
      } else if ((characters = node.getContentDescription()) != null) {
        sb.append('[');
        sb.append(characters);
        sb.append(']');
      } else if (!ScreenUtilities.isEditable(node)) {
        sb.append('(');
        sb.append(ScreenUtilities.getClassName(node));
        sb.append(')');
      }

      text = sb.toString();
    }

    synchronized (this) {
      setText(text, indent);
      resetNode();
      currentNode = AccessibilityNodeInfo.obtain(node);
      currentDescribe = describe;

      if (isEditable()) {
        int start = getSelectionStart();
        int end = getSelectionEnd();

        if (isSelected(end)) {
          adjustRight(setLine(end-1), 1);
        }

        if (isSelected(start)) {
          adjustLeft(setLine(start), 0);
        }
      }

      return write();
    }
  }

  public boolean write (AccessibilityNodeInfo node, boolean force) {
    if (node == null) return false;

    synchronized (this) {
      int indent = getLineIndent();

      if (!node.equals(currentNode)) {
        if (!force) return false;
        indent = 0;
      }

      return write(node, currentDescribe, indent);
    }
  }

  @Override
  public boolean isEditable () {
    return ScreenUtilities.isEditable(currentNode);
  }

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final InputConnection getInputConnection () {
    return InputService.getInputConnection();
  }

  @Override
  public boolean insertText (String string) {
    InputService service = getInputService();

    if (service != null) {
      if (service.insertText(string)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean deleteText (int start, int end) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.beginBatchEdit()) {
        if (setCursor(end)) {
          if (connection.deleteSurroundingText((end - start), 0)) {
            if (connection.endBatchEdit()) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  @Override
  public boolean setSelection (int start, int end) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.setSelection(start, end)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean setCursor (int offset) {
    return setSelection(offset, offset);
  }

  private final static String[] keysFileNames = new String[] {
    "nabcc", "common", "edit", "host", "developer"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public HostEndpoint () {
    super();
    resetNode();
    write("no screen monitor");
  }
}
