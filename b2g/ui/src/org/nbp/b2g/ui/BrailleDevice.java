package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import android.graphics.Rect;

import android.view.accessibility.AccessibilityNodeInfo;

public class BrailleDevice {
  private final static String LOG_TAG = BrailleDevice.class.getName();

  public final static Object LOCK = new Object();
  private static byte[] brailleCells = null;

  public final static byte DOT_1 =       0X01;
  public final static byte DOT_2 =       0X02;
  public final static byte DOT_3 =       0X04;
  public final static byte DOT_4 =       0X08;
  public final static byte DOT_5 =       0X10;
  public final static byte DOT_6 =       0X20;
  public final static byte DOT_7 =       0X40;
  public final static byte DOT_8 = (byte)0X80;

  private static Map<Character, Byte> characterMap = new HashMap<Character, Byte>();

  public static void unsetCharacters () {
    characterMap.clear();
  }

  public static boolean setCharacter (char character, byte dots) {
    if (characterMap.get(character) != null) return false;
    characterMap.put(character, dots);
    return true;
  }

  public static boolean setCharacter (char character, int keyMask) {
    if (keyMask == 0) return false;
    if (keyMask == KeyMask.SPACE) keyMask = 0;
    if ((keyMask & ~KeyMask.DOTS_ALL) != 0) return false;

    byte dots = 0;
    if ((keyMask & KeyMask.DOT_1) != 0) dots |= BrailleDevice.DOT_1;
    if ((keyMask & KeyMask.DOT_2) != 0) dots |= BrailleDevice.DOT_2;
    if ((keyMask & KeyMask.DOT_3) != 0) dots |= BrailleDevice.DOT_3;
    if ((keyMask & KeyMask.DOT_4) != 0) dots |= BrailleDevice.DOT_4;
    if ((keyMask & KeyMask.DOT_5) != 0) dots |= BrailleDevice.DOT_5;
    if ((keyMask & KeyMask.DOT_6) != 0) dots |= BrailleDevice.DOT_6;
    if ((keyMask & KeyMask.DOT_7) != 0) dots |= BrailleDevice.DOT_7;
    if ((keyMask & KeyMask.DOT_8) != 0) dots |= BrailleDevice.DOT_8;

    return setCharacter(character, dots);
  }

  private static String textString;
  private static int lineStart;
  private static String lineText;
  private static int lineIndent;

  private static int setLine (int textOffset) {
    lineStart = textString.lastIndexOf('\n', textOffset-1) + 1;
    int lineLength = textString.indexOf('\n', lineStart);
    if (lineLength == -1) lineLength = textString.length();
    lineText = textString.substring(lineStart, lineLength);
    return textOffset - lineStart;
  }

  private static void setText (String text, int indent) {
    textString = text;
    setLine(0);
    lineIndent = indent;
  }

  private static void setText (String text) {
    setText(text, 0);
  }

  public static int getLineStart () {
    return lineStart;
  }

  public static int getLineLength () {
    return lineText.length();
  }

  public static int getLineIndent () {
    return lineIndent;
  }

  public static int getBrailleStart () {
    return lineStart + lineIndent;
  }

  private static AccessibilityNodeInfo currentNode = null;
  private static boolean currentDescribe = false;

  private static void resetNode () {
    if (currentNode != null) {
      currentNode.recycle();
      currentNode = null;
    }

    currentDescribe = false;
  }

  public final static int NO_SELECTION = -1;
  private static int selectionStart = NO_SELECTION;
  private static int selectionEnd = NO_SELECTION;

  public static boolean isSelected (int offset) {
    return offset != NO_SELECTION;
  }

  public static boolean isSelected (int start, int end) {
    return (start != end) && isSelected(start) && isSelected(end);
  }

  public static boolean isSelected () {
    synchronized (LOCK) {
      return isSelected(selectionStart, selectionEnd);
    }
  }

  public static int getSelectionStart () {
    return selectionStart;
  }

  public static int getSelectionEnd () {
    return selectionEnd;
  }

  private static void adjustLeft (int offset, int keep) {
    if (offset < lineIndent) {
      int newIndent = offset - keep;
      if (newIndent < 0) newIndent = 0;
      lineIndent = newIndent;
    }
  }

  private static void adjustRight (int offset, int keep) {
    if (offset >= (lineIndent + brailleCells.length)) {
      lineIndent = offset + keep - brailleCells.length;
    }
  }

  public static void setSelection (int start, int end) {
    synchronized (LOCK) {
      selectionStart = start;
      selectionEnd = end;

      if ((start == end) && isSelected(start)) {
        int offset = setLine(start);
        int keep = ApplicationParameters.BRAILLE_SCROLL_KEEP;

        adjustLeft(offset, keep);
        adjustRight(offset, keep);
      }

      write();
    }
  }

  public static void clearSelection () {
    setSelection(NO_SELECTION, NO_SELECTION);
  }

  private native static boolean openDevice ();
  private native static void closeDevice ();

  private native static String getVersion ();
  private native static int getCellCount ();

  private native static boolean clearCells ();
  private native static boolean writeCells (byte[] cells);

  public static boolean open () {
    if (brailleCells != null) return true;

    if (openDevice()) {
      int cellCount = getCellCount();

      if (cellCount > 0) {
        brailleCells = new byte[cellCount];
        return true;
      }

      closeDevice();
    }

    return false;
  }

  private static Timer delayTimer = null;
  private static boolean delayUpdate = false;

  private static boolean write () {
    if (delayTimer != null) {
      delayUpdate = true;
      return true;
    }

    if (open()) {
      {
        int length = lineText.length();
        int count = length - lineIndent;
        if (count > brailleCells.length) count = brailleCells.length;
        int toIndex = 0;

        while (toIndex < count) {
          int fromIndex = toIndex + lineIndent;
          char character = (fromIndex < length)? lineText.charAt(fromIndex): ' ';
          Byte dots = characterMap.get(character);
          brailleCells[toIndex++] = (dots != null)? dots: ApplicationParameters.BRAILLE_CHARACTER_UNDEFINED;
        }

        while (toIndex < brailleCells.length) brailleCells[toIndex++] = 0;
      }

      if (currentNode != null) {
        if (ScreenUtilities.isEditable(currentNode)) {
          int start = selectionStart;
          int end = selectionEnd;

          if (isSelected(start) && isSelected(end)) {
            int brailleStart = getBrailleStart();
            int nextLine = lineText.length() - lineIndent + 1;

            if ((start -= brailleStart) < 0) start = 0;
            if ((end -= brailleStart) > nextLine) end = nextLine;

            if (start == end) {
              if (end < brailleCells.length) {
                brailleCells[end] |= ApplicationParameters.BRAILLE_OVERLAY_CURSOR;
              }
            } else {
              if (end > brailleCells.length) end = brailleCells.length;

              while (start < end) {
                brailleCells[start++] |= ApplicationParameters.BRAILLE_OVERLAY_SELECTED;
              }
            }
          }
        }
      }

      if (writeCells(brailleCells)) {
        TimerTask task = new TimerTask() {
          @Override
          public void run () {
            synchronized (LOCK) {
              delayTimer.cancel();
              delayTimer = null;

              if (delayUpdate) {
                delayUpdate = false;
                write();
              }
            }
          }
        };

        delayUpdate = false;
        delayTimer = new Timer();
        delayTimer.schedule(task, ApplicationParameters.BRAILLE_UPDATE_DELAY);
        return true;
      }
    }

    return false;
  }

  public static boolean write (String text) {
    synchronized (LOCK) {
      setText(text);
      resetNode();
      return write();
    }
  }

  private static String getClassName (AccessibilityNodeInfo node) {
    String name = node.getClassName().toString();
    int index = name.lastIndexOf('.');
    return name.substring(index+1);
  }

  private static boolean write (AccessibilityNodeInfo node, boolean describe, int indent) {
    StringBuilder sb = new StringBuilder();
    String string;
    CharSequence characters;

    if (describe) {
      sb.append(getClassName(node));

      if ((characters = node.getText()) != null) {
        sb.append(" \"");
        sb.append(characters);
        sb.append('"');
      }

      if ((characters = node.getContentDescription()) != null) {
        sb.append(" (");
        sb.append(characters);
        sb.append(')');
      }

      sb.append(' ');
      if (node.isFocusable()) sb.append('i');
      if (node.isScrollable()) sb.append('s');
      if (node.isCheckable()) sb.append('c');
      if (node.isVisibleToUser()) sb.append('V');
      if (node.isEnabled()) sb.append('E');
      if (node.isFocused()) sb.append('I');
      if (node.isAccessibilityFocused()) sb.append('A');
      if (node.isSelected()) sb.append('X');
      if (node.isChecked()) sb.append('C');

      {
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);

        sb.append(' ');
        sb.append(bounds.toShortString());
      }
    } else {
      if (node.isCheckable()) {
        sb.append('[');
        sb.append(node.isChecked()? 'X': ' ');
        sb.append("] ");
      }

      if ((characters = node.getText()) != null) {
        sb.append(characters);
      } else if ((characters = node.getContentDescription()) != null) {
        sb.append(characters);
      } else {
        sb.append('(');
        sb.append(getClassName(node));
        sb.append(')');
      }
    }

    synchronized (LOCK) {
      setText(sb.toString(), indent);
      resetNode();
      currentNode = AccessibilityNodeInfo.obtain(node);
      currentDescribe = describe;

      if (ScreenUtilities.isEditable(node)) {
        if (isSelected(selectionEnd)) {
          int end = setLine(selectionEnd);
          adjustRight(end, 1);
        }

        if (isSelected(selectionStart)) {
          int start = setLine(selectionStart);
          adjustLeft(start, 0);
        }
      }

      return write();
    }
  }

  public static boolean write (AccessibilityNodeInfo node, boolean describe) {
    return write(node, describe, 0);
  }

  public static boolean write (AccessibilityNodeInfo node) {
    if (node == null) return false;
    if (!node.equals(currentNode)) return false;
    return write(node, currentDescribe, lineIndent);
  }

  public static boolean shiftRight (int offset) {
    synchronized (LOCK) {
      if (offset < 1) return false;
      if (offset >= brailleCells.length) return false;

      if ((offset += lineIndent) >= lineText.length()) return false;
      lineIndent = offset;
      return write();
    }
  }

  public static boolean panLeft () {
    synchronized (LOCK) {
      if (lineIndent == 0) {
        if (lineStart == 0) return false;
        setLine(lineStart-1);
        lineIndent = lineText.length() + 1;
      } else {
        int lineLength = lineText.length();
        if (lineIndent > lineLength) lineIndent = lineLength;
      }

      if ((lineIndent -= brailleCells.length) < 0) lineIndent = 0;
      return write();
    }
  }

  public static boolean panRight () {
    synchronized (LOCK) {
      int newIndent = lineIndent + brailleCells.length;
      int lineLength = lineText.length();

      if (newIndent > lineLength) {
        int offset = lineStart + lineLength + 1;
        if (offset > textString.length()) return false;
        setLine(offset);
        newIndent = 0;
      }

      lineIndent = newIndent;
      return write();
    }
  }

  private BrailleDevice () {
  }

  static {
    System.loadLibrary("UserInterface");

    if (open()) {
      Log.d(LOG_TAG, "braille cell count: " + brailleCells.length);

      String version = getVersion();
      Log.d(LOG_TAG, "braille device version: " + version);

      clearCells();
      KeyBindings.load();
    }

    setText("");
    resetNode();
  }
}
