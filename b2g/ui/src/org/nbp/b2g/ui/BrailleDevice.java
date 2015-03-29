package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.graphics.Rect;

import android.view.accessibility.AccessibilityNodeInfo;

public class BrailleDevice {
  private final static String LOG_TAG = BrailleDevice.class.getName();

  private static int cellCount = 0;

  private static String currentText = "";
  private static int currentIndent = 0;

  private static AccessibilityNodeInfo currentNode = null;
  private static boolean currentDescribe = false;

  private static Map<Character, Byte> characterMap = new HashMap<Character, Byte>();

  public native static boolean openDevice ();
  public native static void closeDevice ();

  public native static String getVersion ();
  public native static int getCellCount ();

  public native static boolean clearCells ();
  public native static boolean writeCells (byte[] cells);

  public static boolean setCharacter (char character, int keyMask) {
    if (keyMask == KeyMask.SPACE) keyMask = 0;
    int dots = keyMask & KeyMask.DOTS_12345678;
    if (dots != keyMask) return false;

    characterMap.put(character, (byte)dots);
    return true;
  }

  private static boolean open () {
    if (openDevice()) {
      if (cellCount != 0) return true;
      if ((cellCount = getCellCount()) != 0) return true;
    }

    return false;
  }

  private static boolean write () {
    if (open()) {
      int length = currentText.length();
      int count = length - currentIndent;
      byte[] cells = new byte[count];

      for (int toIndex=0; toIndex<count; toIndex+=1) {
        int fromIndex = toIndex + currentIndent;
        char character = (fromIndex < length)? currentText.charAt(fromIndex): ' ';
        Byte dots = characterMap.get(character);
        cells[toIndex] = (dots != null)? dots: (byte)ApplicationParameters.UNDEFINED_BRAILLE_CHARACTER;
      }

      if (writeCells(cells)) {
        return true;
      }
    }

    return false;
  }

  private static void reset () {
    currentText = null;
    currentIndent = 0;
    currentDescribe = false;

    if (currentNode != null) {
      currentNode.recycle();
      currentNode = null;
    }
  }

  public static boolean write (String text) {
    reset();
    currentText = text;
    return write();
  }

  private static String getClassName (AccessibilityNodeInfo node) {
    String name = node.getClassName().toString();
    int index = name.lastIndexOf('.');
    return name.substring(index+1);
  }

  public static boolean write (AccessibilityNodeInfo node, boolean describe) {
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

    string = sb.toString();
    if ((currentNode == null) || !currentNode.equals(node)) {
      reset();
      currentNode = AccessibilityNodeInfo.obtain(node);
    }

    currentText = string;
    currentDescribe = describe;
    return write();
  }

  public static boolean write (AccessibilityNodeInfo node) {
    return write(node, currentDescribe);
  }

  public static boolean moveLeft () {
    if (currentIndent == 0) return false;

    int length = currentText.length();
    if (currentIndent > length) currentIndent = length;

    if ((currentIndent -= cellCount) < 0) currentIndent = 0;
    return write();
  }

  public static boolean moveRight () {
    int length = currentText.length();
    if ((currentIndent + cellCount) >= length) return false;

    currentIndent += cellCount;
    return write();
  }

  private BrailleDevice () {
  }

  static {
    System.loadLibrary("UserInterface");

    if (openDevice()) {
      String version = getVersion();
      Log.d(LOG_TAG, "braille device version: " + version);

      int cellCount = getCellCount();
      Log.d(LOG_TAG, "braille cell count: " + cellCount);

      clearCells();
      KeyBindings.load();
    }
  }
}
