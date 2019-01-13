package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import java.util.Map;
import java.util.HashMap;

import org.nbp.common.Braille;
import org.nbp.common.speech.SpeechSpan;

import android.util.Log;

import android.os.Bundle;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

import android.widget.RadioButton;

import android.text.Spanned;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;

public class HostEndpoint extends InputEndpoint {
  private final static String LOG_TAG = HostEndpoint.class.getName();

  protected final InputService getInputService () {
    return InputService.getInputService();
  }

  protected final InputConnection getInputConnection () {
    return InputService.getInputConnection();
  }

  private AccessibilityNodeInfo currentNode = null;
  private boolean currentDescribe = false;

  private void resetNode () {
    if (currentNode != null) {
      currentNode.recycle();
      currentNode = null;
    }

    currentDescribe = false;
  }

  public final AccessibilityNodeInfo getCurrentNode () {
    synchronized (this) {
      if (currentNode == null) return null;
      return AccessibilityNodeInfo.obtain(currentNode);
    }
  }

  private final boolean canAssign (Class to) {
    synchronized (this) {
      if (currentNode == null) return false;
      return ScreenUtilities.canAssign(to, currentNode);
    }
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, String text) {
    sb.setSpan(new SpeechSpan(text), start, sb.length(), sb.SPAN_EXCLUSIVE_EXCLUSIVE);
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, CharSequence text) {
    setSpeechSpan(sb, start, text.toString());
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, int text) {
    setSpeechSpan(sb, start, getString(text));
  }

  private final static void appendElementState (SpannableStringBuilder sb, String word) {
    int start = sb.length();

    sb.append(" (");
    sb.append(word);
    sb.append(')');

    setSpeechSpan(sb, start, word);
  }

  private final static void appendElementState (SpannableStringBuilder sb, int word) {
    appendElementState(sb, getString(word));
  }

  private final CharSequence toText (AccessibilityNodeInfo node) {
    SpannableStringBuilder sb = new SpannableStringBuilder();
    int textInsertionOffset = -1;
    CharSequence text;

    if (node.isCheckable()) {
      int start = sb.length();

      boolean isChecked = node.isChecked();
      boolean isSingle = canAssign(RadioButton.class);

      final char begin;
      final char end;
      final int word;

      if (isSingle) {
        begin = '⢎';
        end   = '⡱';
        word  = isChecked? R.string.choice_single_on: R.string.choice_single_off;
      } else {
        begin = '⣏';
        end   = '⣹';
        word  = isChecked? R.string.choice_multiple_on: R.string.choice_multiple_off;
      }

      sb.append(begin);
      sb.append(isChecked? '⠶': ' ');
      sb.append(end);

      sb.append(' ');
      setSpeechSpan(sb, start, word);
    }

    if ((text = node.getText()) == null) {
      text = AccessibilityText.get(node);
    }

    if (text != null) {
      textInsertionOffset = sb.length();
    } else if (ScreenUtilities.isEditable(node)) {
      if (node.isPassword()) {
        int end = getSelectionEnd();

        if (isSelected(end)) {
          char character = ApplicationParameters.PASSWORD_CHARACTER;

          while (end > 0) {
            sb.append(character);
            end -= 1;
          }
        }
      }
    } else if ((text = node.getContentDescription()) != null) {
      boolean mark = node.isFocusable() && node.isClickable();

      if (mark) {
        int start = sb.length();

        sb.append('[');
        sb.append(text);
        sb.append(']');

        setSpeechSpan(sb, start, text);
      } else {
        sb.append(text);
      }
    } else {
      String type = ScreenUtilities.getClassName(node);
      int start = sb.length();

      sb.append('{');
      sb.append(type);
      sb.append('}');

      setSpeechSpan(sb, start, Wordify.get(type));
    }

    if (!node.isEnabled()) {
      appendElementState(sb, R.string.state_disabled);
    }

    if (textInsertionOffset >= 0) {
      if (sb.length() == 0) return text;
      sb.insert(textInsertionOffset, text);
    }

    return sb.subSequence(0, sb.length());
  }

  public final boolean write (AccessibilityNodeInfo node, boolean describe, int indent) {
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

      if (!sameNode) {
        resetSpeech();
        changeSelection(NO_SELECTION, NO_SELECTION);
      }

      setText(text, sameNode);

      if (!sameNode) {
        if (isInputArea()) {
          InputService service = getInputService();

          if (service != null) {
            int start;
            int end;

            synchronized (service) {
              start = service.getSelectionStart();
              end = service.getSelectionEnd();
            }

            changeSelection(start, end);
          }
        }

        if (isPasswordField()) {
          ApplicationUtilities.message(R.string.message_password_field);
        }
      }

      return write();
    }
  }

  public final boolean write (AccessibilityNodeInfo node, boolean force) {
    if (node == null) return false;

    boolean describe;
    int indent;

    synchronized (this) {
      describe = currentDescribe;
      indent = getLineIndent();

      if (!node.equals(currentNode)) {
        if (!force) return false;
        indent = 0;

        if (currentNode != null) {
        //AccessibilityText.set(currentNode, null);
          ScreenUtilities.setCurrentNode(node);
        }
      }
    }

    return write(node, describe, indent);
  }

  public final boolean write (AccessibilityNodeInfo node, CharSequence text) {
    synchronized (this) {
      if (!node.equals(currentNode)) return true;
      if (text.equals(getText())) return true;
      setText(text, true);
    }

    return write();
  }

  public final boolean onTextSelectionChange (AccessibilityNodeInfo node, int start, int end) {
    synchronized (this) {
      if (node.equals(currentNode)) {
        return changeSelection(start, end);
      }
    }

    return false;
  }

  public final void onInputSelectionChange (int start, int end) {
    synchronized (this) {
      if (isInputArea()) {
        if (!isSelected(getSelectionStart())) {
          updateSelection(start, end);
        }
      }
    }
  }

  @Override
  public final boolean isInputArea () {
    AccessibilityNodeInfo node = currentNode;
    if (node == null) return false;
    return ScreenUtilities.isEditable(node);
  }

  @Override
  public CharSequence getHintText () {
    InputService service = InputService.getInputService();
    if (service == null) return null;
    return service.getHintText();
  }

  @Override
  public boolean isPasswordField () {
    return currentNode.isPassword();
  }

  @Override
  public final boolean isBar () {
    AccessibilityNodeInfo node = currentNode;
    if (node == null) return false;
    return ScreenUtilities.isBar(node);
  }

  @Override
  public final boolean isSlider () {
    AccessibilityNodeInfo node = currentNode;
    if (node == null) return false;
    return ScreenUtilities.isSlider(node);
  }

  protected boolean performNodeAction (int action, Bundle arguments) {
    if (currentNode == null) return false;
    return currentNode.performAction(action, arguments);
  }

  protected boolean performNodeAction (int action) {
    return performNodeAction(action, null);
  }

  @Override
  public final boolean seekNext () {
    return performNodeAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
  }

  @Override
  public final boolean seekPrevious () {
    return performNodeAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
  }

  private final static CharacterStyle SPAN_BOLD = HighlightSpans.BOLD.getSingleton();
  private final static CharacterStyle SPAN_BOLD_ITALIC = HighlightSpans.BOLD_ITALIC.getSingleton();
  private final static CharacterStyle SPAN_ITALIC = HighlightSpans.ITALIC.getSingleton();
  private final static CharacterStyle SPAN_STRIKE = HighlightSpans.STRIKE.getSingleton();
  private final static CharacterStyle SPAN_UNDERLINE = HighlightSpans.UNDERLINE.getSingleton();

  private static CharSequence addSpans (CharSequence text) {
    if (text.length() > 0) {
      Object[] spans = new Object[3];
      int count = 0;

      if (ApplicationSettings.TYPING_BOLD && ApplicationSettings.TYPING_ITALIC) {
        spans[count++] = SPAN_BOLD_ITALIC;
      } else if (ApplicationSettings.TYPING_BOLD) {
        spans[count++] = SPAN_BOLD;
      } else if (ApplicationSettings.TYPING_ITALIC) {
        spans[count++] = SPAN_ITALIC;
      }

      if (ApplicationSettings.TYPING_STRIKE) {
        spans[count++] = SPAN_STRIKE;
      }

      if (ApplicationSettings.TYPING_UNDERLINE) {
        spans[count++] = SPAN_UNDERLINE;
      }

      if (count > 0) {
        int start = 0;
        int end = text.length();
        int flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

        if (text instanceof Spannable) {
          Spannable spannable = (Spannable)text;

          for (int index=0; index<count; index+=1) {
            spannable.setSpan(spans[index], start, end, flags);
          }
        } else {
          SpannableStringBuilder sb = new SpannableStringBuilder(text);

          for (int index=0; index<count; index+=1) {
            sb.setSpan(spans[index], start, end, flags);
          }

          text = sb.subSequence(start, end);
        }
      }
    }

    return text;
  }

  private boolean textChangePending = false;

  public final boolean onTextChange (AccessibilityNodeInfo node) {
    synchronized (this) {
      if (!node.equals(currentNode)) return false;
      setText(toText(node), true);

      if (textChangePending) {
        textChangePending = false;
        notifyAll();
      }

      return true;
    }
  }

  private interface TextChangeFunction {
    public boolean changeText ();
  }

  private final boolean performTextChangeFunction (TextChangeFunction function) {
    synchronized (this) {
      if (!function.changeText()) return false;
      textChangePending = true;

      long startTime = System.currentTimeMillis();
      long timeout = ApplicationParameters.TEXT_CHANGE_TIMEOUT;

      do {
        try {
          wait(timeout);
        } catch (InterruptedException exception) {
        }

        if (!textChangePending) return true;
      } while ((System.currentTimeMillis() - startTime) < timeout);

      Log.w(LOG_TAG, "text change wait timeout");
      textChangePending = false;
      return true;
    }
  }

  @Override
  public final boolean replaceText (final int start, final int end, final CharSequence text) {
    final InputConnection connection = getInputConnection();
    if (connection == null) return false;

    TextChangeFunction function = new TextChangeFunction() {
      @Override
      public boolean changeText () {
        return connection.setComposingRegion(start, end)
            && connection.commitText(addSpans(text), 1);
      }
    };

    return performTextChangeFunction(function);
  }

  @Override
  public final boolean insertText (final CharSequence text) {
    final InputConnection connection = getInputConnection();
    if (connection == null) return false;

    TextChangeFunction function = new TextChangeFunction() {
      @Override
      public boolean changeText () {
        return connection.commitText(addSpans(text), 1);
      }
    };

    return performTextChangeFunction(function);
  }

  @Override
  public final boolean setSelection (int start, int end) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.setSelection(start, end)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public final Class<? extends Action> getMoveBackwardAction () {
    return MoveBackward.class;
  }

  @Override
  public final Class<? extends Action> getMoveForwardAction () {
    return MoveForward.class;
  }

  @Override
  public final Class<? extends Action> getScrollBackwardAction () {
    return MovePrevious.class;
  }

  @Override
  public final Class<? extends Action> getScrollForwardAction () {
    return MoveNext.class;
  }

  @Override
  public final Class<? extends Action> getScrollFirstAction () {
    return ScrollToFirst.class;
  }

  @Override
  public final Class<? extends Action> getScrollLastAction () {
    return ScrollToLast.class;
  }

  @Override
  public final boolean handleKeyboardKey_enter () {
    return InputService.injectKey(KeyEvent.KEYCODE_ENTER);
  }

  @Override
  public final boolean handleKeyboardKey_cursorLeft () {
    if (isInputArea()) return super.handleKeyboardKey_cursorLeft();
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_LEFT);
  }

  @Override
  public final boolean handleKeyboardKey_cursorRight () {
    if (isInputArea()) return super.handleKeyboardKey_cursorRight();
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_RIGHT);
  }

  @Override
  public final boolean handleKeyboardKey_cursorUp () {
    if (isInputArea()) return super.handleKeyboardKey_cursorUp();
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_UP);
  }

  @Override
  public final boolean handleKeyboardKey_cursorDown () {
    if (isInputArea()) return super.handleKeyboardKey_cursorDown();
    return InputService.injectKey(KeyEvent.KEYCODE_DPAD_DOWN);
  }

  @Override
  public final boolean handleKeyboardKey_pageUp () {
    if (isInputArea()) return super.handleKeyboardKey_pageUp();
    return InputService.injectKey(KeyEvent.KEYCODE_PAGE_UP);
  }

  @Override
  public final boolean handleKeyboardKey_pageDown () {
    if (isInputArea()) return super.handleKeyboardKey_pageDown();
    return InputService.injectKey(KeyEvent.KEYCODE_PAGE_DOWN);
  }

  @Override
  public final boolean handleKeyboardKey_home () {
    if (isInputArea()) return super.handleKeyboardKey_home();
    return InputService.injectKey(KeyEvent.KEYCODE_MOVE_HOME);
  }

  @Override
  public final boolean handleKeyboardKey_end () {
    if (isInputArea()) return super.handleKeyboardKey_end();
    return InputService.injectKey(KeyEvent.KEYCODE_MOVE_END);
  }

  private final static Map<Character, Movements.Movement> movementMap
             = new HashMap<Character, Movements.Movement>();

  private final void mapMovementCharacter (char character, Movements.Movement movement) {
    movementMap.put(character, movement);
  }

  private final void mapMovementCharacters () {
    mapMovementCharacter('(', Movements.CHARACTER);
    mapMovementCharacter(')', Movements.WORD);
    mapMovementCharacter('_', Movements.LINE);
    mapMovementCharacter('{', Movements.PARAGRAPH);
    mapMovementCharacter('}', Movements.PAGE);

    mapMovementCharacter('|', Movements.PARENT_OR_FIRST_CHILD);
    mapMovementCharacter('-', Movements.SIBLING);

    mapMovementCharacter('1', Movements.HEADING_LEVEL_1);
    mapMovementCharacter('2', Movements.HEADING_LEVEL_2);
    mapMovementCharacter('3', Movements.HEADING_LEVEL_3);
    mapMovementCharacter('4', Movements.HEADING_LEVEL_4);
    mapMovementCharacter('5', Movements.HEADING_LEVEL_5);
    mapMovementCharacter('6', Movements.HEADING_LEVEL_6);

    mapMovementCharacter('a', Movements.ARTICLE);
    mapMovementCharacter('b', Movements.BUTTON);
    mapMovementCharacter('c', Movements.COMBOBOX);
    mapMovementCharacter('d', Movements.DOCUMENT);
    mapMovementCharacter('e', Movements.EDITABLE_INPUT);
    mapMovementCharacter('f', Movements.FORM_FIELD);
    mapMovementCharacter('g', Movements.GRAPHIC);
    mapMovementCharacter('h', Movements.HEADING);
    mapMovementCharacter('i', Movements.LIST_ITEM);
    mapMovementCharacter('l', Movements.LINK);
    mapMovementCharacter('m', Movements.LANDMARK);
    mapMovementCharacter('o', Movements.LIST);
    mapMovementCharacter('r', Movements.RADIO_BUTTON);
    mapMovementCharacter('t', Movements.TABLE);
    mapMovementCharacter('u', Movements.UNVISITED_LINK);
    mapMovementCharacter('v', Movements.VISITED_LINK);
    mapMovementCharacter('x', Movements.CHECKBOX);
  }

  private final Movements.Movement getMovement (Character character) {
    synchronized (movementMap) {
      if (movementMap.size() == 0) mapMovementCharacters();
      return movementMap.get(character);
    }
  }

  private Movements.Movement currentMovement = null;

  @Override
  public final boolean handleDotKeys (byte dots) {
    Movements.Action action;
    Movements.Movement movement;

    {
      final byte PREVIOUS = Braille.CELL_DOT_7;
      final byte NEXT = Braille.CELL_DOT_8;
      final byte DIRECTION = PREVIOUS | NEXT;

      switch (dots & DIRECTION) {
        case PREVIOUS:
          action = Movements.Action.BACKWARD;
          break;

        case NEXT:
          action = Movements.Action.FORWARD;
          break;

        case PREVIOUS | NEXT:
          action = Movements.Action.SHOW;
          break;

        default:
          return false;
      }

      dots &= ~DIRECTION;
    }

    if (dots == 0) {
      movement = currentMovement;
    } else {
      Character character = Characters.getCharacters().getCharacter(dots);
      if (character == null) return false;
      movement = getMovement(character);
    }

    if (movement == null) return false;
    currentMovement = movement;
    return movement.perform(getCurrentNode(), action);
  }

  public HostEndpoint () {
    super();
    addKeyBindings("host");

    resetNode();
    write(R.string.message_no_screen_monitor);
  }
}
