package org.nbp.b2g.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputConnection;

public class BrailleDevice {
  private final static String LOG_TAG = BrailleDevice.class.getName();

  public final static Object LOCK = new Object();

  public final static byte DOT_1 =       0X01;
  public final static byte DOT_2 =       0X02;
  public final static byte DOT_3 =       0X04;
  public final static byte DOT_4 =       0X08;
  public final static byte DOT_5 =       0X10;
  public final static byte DOT_6 =       0X20;
  public final static byte DOT_7 =       0X40;
  public final static byte DOT_8 = (byte)0X80;

  private static byte[] brailleCells = null;

  public static int getBrailleLength () {
    return brailleCells.length;
  }

  private static String textString;

  public static String getText () {
    return textString;
  }

  public static int getTextLength () {
    return textString.length();
  }

  private static int lineStart;
  private static String lineText;
  private static int lineIndent;

  public static int findPreviousNewline (int offset) {
    return textString.lastIndexOf('\n', offset-1);
  }

  public static int findNextNewline (int offset) {
    return textString.indexOf('\n', offset);
  }

  public static int setLine (int textOffset) {
    lineStart = findPreviousNewline(textOffset) + 1;
    int lineLength = findNextNewline(lineStart);
    if (lineLength == -1) lineLength = getTextLength();
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

  public static void setLineIndent (int indent) {
    lineIndent = indent;
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

  public static String getSelectedText () {
    synchronized (LOCK) {
      if (isSelected()) {
        return textString.substring(selectionStart, selectionEnd);
      }
    }

    return null;
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

  public static boolean write () {
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
          Byte dots = Context.getCurrentContext().getCharacters().getDots(character);
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

  public static boolean write (AccessibilityNodeInfo node, boolean describe, int indent) {
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
        sb.append(characters);
      } else {
        sb.append('(');
        sb.append(ScreenUtilities.getClassName(node));
        sb.append(')');
      }

      text = sb.toString();
    }

    synchronized (LOCK) {
      setText(text, indent);
      resetNode();
      currentNode = AccessibilityNodeInfo.obtain(node);
      currentDescribe = describe;

      if (ScreenUtilities.isEditable(node)) {
        if (isSelected(selectionEnd)) {
          int end = setLine(selectionEnd-1);
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

  public static boolean write (AccessibilityNodeInfo node, boolean force) {
    if (node == null) return false;

    synchronized (LOCK) {
      int indent = lineIndent;

      if (!node.equals(currentNode)) {
        if (!force) return false;
        indent = 0;
      }

      return write(node, currentDescribe, indent);
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
    }

    setText("");
    resetNode();
  }
}
