package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.os.Bundle;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

import android.text.Spanned;
import android.text.SpannableStringBuilder;

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

  private final Map<AccessibilityNodeInfo, CharSequence> accessibilityText = new HashMap<AccessibilityNodeInfo, CharSequence>();

  public void setAccessibilityText (AccessibilityNodeInfo node, CharSequence text) {
    if (text != null) {
      accessibilityText.put(AccessibilityNodeInfo.obtain(node), text);
    } else {
      accessibilityText.remove(node);
    }
  }

  private final CharSequence getAccessibilityText (AccessibilityNodeInfo node) {
    return accessibilityText.get(node);
  }

  private static CharSequence toText (CharSequence text) {
    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);

      if (spans != null) {
        if (spans.length > 0) {
          SpannableStringBuilder sb = new SpannableStringBuilder(text);

          /*
          for (Object span : spans) {
            Log.d(LOG_TAG, "span object: " + span.getClass().getName());
          }

          sb.append('|');
          sb.append(spans.length);
          */

          return sb.subSequence(0, sb.length());
        }
      }
    }

    return text.subSequence(0, text.length());
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, String text) {
    sb.setSpan(new SpeechSpan(text), start, sb.length(), 0);
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, CharSequence text) {
    setSpeechSpan(sb, start, text.toString());
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, int text) {
    setSpeechSpan(sb, start, ApplicationContext.getString(text));
  }

  private final static Map<String, String> wordifiedTypes = new HashMap<String, String>();

  private final static String getWordifiedType (String type) {
    String text = wordifiedTypes.get(type);
    if (text != null) return text;

    text = type.replaceAll("(?<=\\p{Lower})()(?=\\p{Upper})", " ");
    wordifiedTypes.put(type, text);
    return text;
  }

  private final static void appendElementState (SpannableStringBuilder sb, String word) {
    int start = sb.length();

    sb.append(" (");
    sb.append(word);
    sb.append(')');

    setSpeechSpan(sb, start, word);
  }

  private final static void appendElementState (SpannableStringBuilder sb, int word) {
    appendElementState(sb, ApplicationContext.getString(word));
  }

  private final CharSequence toText (AccessibilityNodeInfo node) {
    SpannableStringBuilder sb = new SpannableStringBuilder();
    CharSequence characters;

    if (node.isCheckable()) {
      boolean isChecked = node.isChecked();
      int start = sb.length();

      sb.append('⣏');
      sb.append(isChecked? '⠶': ' ');
      sb.append("⣹ ");

      setSpeechSpan(sb, start, (
        isChecked?
        R.string.checkbox_on:
        R.string.checkbox_off
      ));
    }

    if ((characters = node.getText()) == null) {
      characters = getAccessibilityText(node);
    }

    if (characters != null) {
      sb.append(toText(characters));
    } else if (ScreenUtilities.isEditable(node)) {
      int end = getSelectionEnd();

      if (isSelected(end)) {
        while (end > 0) {
          sb.append(' ');
          end -= 1;
        }
      }
    } else if ((characters = node.getContentDescription()) != null) {
      int start = sb.length();

      sb.append('[');
      sb.append(toText(characters));
      sb.append(']');

      setSpeechSpan(sb, start, characters);
    } else {
      String type = ScreenUtilities.getClassName(node);
      int start = sb.length();

      sb.append('{');
      sb.append(type);
      sb.append('}');

      setSpeechSpan(sb, start, getWordifiedType(type));
    }

    if (!node.isEnabled()) {
      appendElementState(sb, R.string.state_disabled);
    }

    return sb.subSequence(0, sb.length());
  }

  public boolean write (AccessibilityNodeInfo node, boolean describe, int indent) {
    CharSequence text;

    if (describe) {
      text = ScreenUtilities.toString(node);
    } else {
      text = toText(node);
    }

    synchronized (this) {
      boolean sameNode = node.equals(currentNode);

      resetNode();
      currentNode = AccessibilityNodeInfo.obtain(node);
      currentDescribe = describe;

      if (!sameNode) resetSpeech();
      setText(text, sameNode);

      if (isInputArea()) {
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
        if (currentNode != null) setAccessibilityText(currentNode, null);
        indent = 0;

        if (currentNode != null) {
          ScreenUtilities.setCurrentNode(node);
        }
      }
    }

    return write(node, describe, indent);
  }

  public boolean write (AccessibilityNodeInfo node, CharSequence text) {
    synchronized (this) {
      if (!node.equals(currentNode)) return true;
      if (text.equals(getText())) return true;
      setText(text, true);
    }

    return write();
  }

  @Override
  public boolean isInputArea () {
    return ScreenUtilities.isEditable(currentNode);
  }

  @Override
  public boolean isBar () {
    return ScreenUtilities.isBar(currentNode);
  }

  @Override
  public boolean isSlider () {
    return ScreenUtilities.isSlider(currentNode);
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
  public boolean insertText (CharSequence text) {
    InputService service = getInputService();

    if (service != null) {
      if (service.insertText(text)) {
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
  public Class<? extends Action> getMoveBackwardAction () {
    return MoveBackward.class;
  }

  @Override
  public Class<? extends Action> getMoveForwardAction () {
    return MoveForward.class;
  }

  @Override
  public Class<? extends Action> getScrollBackwardAction () {
    return MovePrevious.class;
  }

  @Override
  public Class<? extends Action> getScrollForwardAction () {
    return MoveNext.class;
  }

  @Override
  public Class<? extends Action> getScrollFirstAction () {
    return MoveFirst.class;
  }

  @Override
  public Class<? extends Action> getScrollLastAction () {
    return MoveLast.class;
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

  public HostEndpoint () {
    super("host");
    resetNode();
    write(R.string.message_no_screen_monitor);
  }
}
