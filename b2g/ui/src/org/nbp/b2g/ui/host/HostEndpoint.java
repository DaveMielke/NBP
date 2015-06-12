package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.util.Log;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

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

  public AccessibilityNodeInfo getCurrentNode () {
    synchronized (this) {
      if (currentNode == null) return null;
      return AccessibilityNodeInfo.obtain(currentNode);
    }
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
      boolean sameNode = node.equals(currentNode);
      if (!sameNode) resetSpeech();
      setText(text, sameNode);

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
    }

    return write();
  }

  public boolean write (AccessibilityNodeInfo node, boolean force) {
    if (node == null) return false;

    boolean describe;
    int indent;

    synchronized (this) {
      describe = currentDescribe;
      indent = getLineIndent();

      if (!node.equals(currentNode)) {
        if (!force) return false;
        indent = 0;
        ScreenUtilities.setCurrentNode(node);
      }
    }

    return write(node, describe, indent);
  }

  @Override
  public boolean isEditable () {
    return ScreenUtilities.isEditable(currentNode);
  }

  @Override
  public boolean isSeekable () {
    return ScreenUtilities.isSeekable(currentNode);
  }

  protected boolean performNodeAction (int action, Bundle arguments) {
    if (currentNode == null) return false;
    return currentNode.performAction(action, arguments);
  }

  protected boolean performNodeAction (int action) {
    return performNodeAction(action, null);
  }

  @Override
  public boolean seekNext () {
    return performNodeAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
  }

  @Override
  public boolean seekPrevious () {
    return performNodeAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
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

  @Override
  protected String getPanLeftEndAction () {
    return "MoveBackward";
  }

  @Override
  protected String getPanRightEndAction () {
    return "MoveForward";
  }

  @Override
  public boolean handleKeyboardKey_enter () {
    return InputService.injectKey(KeyEvent.KEYCODE_ENTER);
  }

  @Override
  public boolean handleKeyboardKey_cursorLeft () {
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_LEFT);
  }

  @Override
  public boolean handleKeyboardKey_cursorRight () {
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_RIGHT);
  }

  @Override
  public boolean handleKeyboardKey_cursorUp () {
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_UP);
  }

  @Override
  public boolean handleKeyboardKey_cursorDown () {
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_DOWN);
  }

  @Override
  public boolean handleKeyboardKey_pageUp () {
    return InputService.injectKey(KeyEvent.KEYCODE_PAGE_UP);
  }

  @Override
  public boolean handleKeyboardKey_pageDown () {
    return InputService.injectKey(KeyEvent.KEYCODE_PAGE_DOWN);
  }

  @Override
  public boolean handleKeyboardKey_home () {
    return InputService.injectKey(KeyEvent.KEYCODE_MOVE_HOME);
  }

  @Override
  public boolean handleKeyboardKey_end () {
    return InputService.injectKey(KeyEvent.KEYCODE_MOVE_END);
  }

  private final static String[] keysFileNames = new String[] {
    "nabcc", "common", "speech", "edit", "host"
  };

  @Override
  protected String[] getKeysFileNames () {
    return keysFileNames;
  }

  public HostEndpoint () {
    super();
    resetNode();
    write(R.string.message_no_screen_monitor);
  }
}
