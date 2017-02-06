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
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;

public class HostEndpoint extends Endpoint {
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

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, String text) {
    sb.setSpan(new SpeechSpan(text), start, sb.length(), sb.SPAN_EXCLUSIVE_EXCLUSIVE);
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, CharSequence text) {
    setSpeechSpan(sb, start, text.toString());
  }

  private static void setSpeechSpan (SpannableStringBuilder sb, int start, int text) {
    setSpeechSpan(sb, start, ApplicationContext.getString(text));
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
    int textInsertionOffset = -1;
    CharSequence text;

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

    if ((text = node.getText()) == null) {
      text = AccessibilityText.get(node);
    }

    if (text != null) {
      textInsertionOffset = sb.length();
    } else if (ScreenUtilities.isEditable(node)) {
      int end = getSelectionEnd();

      if (isSelected(end)) {
        char character = isPasswordField()? ApplicationParameters.PASSWORD_CHARACTER: ' ';

        while (end > 0) {
          sb.append(character);
          end -= 1;
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
        clearSelection();
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
    return MoveFirst.class;
  }

  @Override
  public final Class<? extends Action> getScrollLastAction () {
    return MoveLast.class;
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

  private enum MovementAction {
    FORWARD,
    BACKWARD,
    SHOW;
  }

  private interface Movement {
    public boolean perform (MovementAction action);
  }

  private abstract class MovementMap<T> {
    public abstract int getForwardAction ();
    public abstract int getBackwardAction ();
    public abstract String getArgumentName ();

    protected abstract void mapCharacters ();
    protected abstract void setArgument (Bundle arguments, String name, T value);

    private class MapValue implements Movement {
      private final T argumentValue;
      private final String descriptiveText;

      public MapValue (T value, String text) {
        argumentValue = value;
        descriptiveText = text;
      }

      public final T getArgumentValue () {
        return argumentValue;
      }

      public final String getDescriptiveText () {
        return descriptiveText;
      }

      private final boolean performAction (int action) {
        Bundle arguments = new Bundle();
        setArgument(arguments, getArgumentName(), getArgumentValue());
        return performNodeAction(action, arguments);
      }

      @Override
      public boolean perform (MovementAction action) {
        switch (action) {
          case FORWARD:
            return performAction(getForwardAction());

          case BACKWARD:
            return performAction(getBackwardAction());

          case SHOW:
            ApplicationUtilities.message(getDescriptiveText());
            return true;
        }

        return false;
      }
    }

    private final Map<Character, MapValue> map
        = new HashMap<Character, MapValue>();

    protected final void mapCharacter (Character character, T value, String text) {
      map.put(character, new MapValue(value, text));
    }

    public final Movement getMovement (Character character) {
      synchronized (map) {
        if (map.size() == 0) mapCharacters();
        return map.get(character);
      }
    }

    protected MovementMap () {
    }
  }

  private class ElementMovementMap extends MovementMap<String> {
    @Override
    public final int getForwardAction () {
      return AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT;
    }

    @Override
    public final int getBackwardAction () {
      return AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT;
    }

    @Override
    public final String getArgumentName () {
      return AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING;
    }

    @Override
    protected final void mapCharacters () {
      mapCharacter('p', "PARENT_FIRST_CHILD", "Parent / First Child");
      mapCharacter('s', "SIBLING", "Sibling");

      mapCharacter('1', "H1", "Level 1 Heading");
      mapCharacter('2', "H2", "Level 2 Heading");
      mapCharacter('3', "H3", "Level 3 Heading");
      mapCharacter('4', "H4", "Level 4 Heading");
      mapCharacter('5', "H5", "Level 5 Heading");
      mapCharacter('6', "H6", "Level 6 Heading");

      mapCharacter('a', "ARTICLE", "Article");
      mapCharacter('b', "BUTTON", "Button");
      mapCharacter('c', "COMBOBOX", "Combo Box");
      mapCharacter('d', "MAIN", "Document");
      mapCharacter('e', "TEXT_FIELD", "Editable Input");
      mapCharacter('f', "CONTROL", "Form Field");
      mapCharacter('g', "GRAPHIC", "Graphic");
      mapCharacter('h', "HEADING", "Heading");
      mapCharacter('i', "LIST_ITEM", "List Item");
      mapCharacter('l', "LINK", "Link");
      mapCharacter('m', "LANDMARK", "Land Mark");
      mapCharacter('o', "LIST", "List");
      mapCharacter('r', "RADIO", "Radio Button");
      mapCharacter('t', "TABLE", "Table");
      mapCharacter('u', "UNVISITED_LINK", "Unvisited Link");
      mapCharacter('v', "VISITED_LINK", "Visited Link");
      mapCharacter('x', "CHECKBOX", "Check Box");
    }

    @Override
    protected final void setArgument (Bundle arguments, String name, String value) {
      arguments.putString(name, value);
    }

    public ElementMovementMap () {
      super();
    }
  }

  private class GranularityMovementMap extends MovementMap<Integer> {
    @Override
    public final int getForwardAction () {
      return AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    }

    @Override
    public final int getBackwardAction () {
      return AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
    }

    @Override
    public final String getArgumentName () {
      return AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT;
    }

    @Override
    protected final void mapCharacters () {
      mapCharacter('7', AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER, "Character");
      mapCharacter('8', AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD, "Word");
      mapCharacter('9', AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE, "Line");
      mapCharacter('0', AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH, "Paragraph");
      mapCharacter('-', AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE, "Page");
    }

    @Override
    protected final void setArgument (Bundle arguments, String name, Integer value) {
      arguments.putInt(name, value);
    }

    public GranularityMovementMap () {
      super();
    }
  }

  private Movement currentMovement = null;
  private final MovementMap[] movementMaps = {
    new ElementMovementMap(),
    new GranularityMovementMap()
  };

  @Override
  public final boolean handleDotKeys (int keyMask) {
    MovementAction action;
    Movement movement;

    {
      final int PREVIOUS = KeyMask.DOT_7;
      final int NEXT = KeyMask.DOT_8;
      final int DIRECTION = PREVIOUS | NEXT;

      switch (keyMask & DIRECTION) {
        case PREVIOUS:
          action = MovementAction.BACKWARD;
          break;

        case NEXT:
          action = MovementAction.FORWARD;
          break;

        case PREVIOUS | NEXT:
          action = MovementAction.SHOW;
          break;

        default:
          return false;
      }

      keyMask &= ~DIRECTION;
    }

    if (keyMask == 0) {
      movement = currentMovement;
    } else {
      Character character = Characters.getCharacters().toCharacter(keyMask);
      if (character == null) return false;
      movement = null;

      for (MovementMap map : movementMaps) {
        movement = map.getMovement(character);
        if (movement != null) break;
      }
    }

    if (movement == null) return false;
    currentMovement = movement;
    return movement.perform(action);
  }

  public HostEndpoint () {
    super("host");
    resetNode();
    write(R.string.message_no_screen_monitor);
  }
}
