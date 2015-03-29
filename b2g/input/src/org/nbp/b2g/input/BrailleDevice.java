package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.view.accessibility.AccessibilityNodeInfo;

public class BrailleDevice {
  private static final String LOG_TAG = BrailleDevice.class.getName();

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

  public static boolean write (String string) {
    if (openDevice()) {
      int length = string.length();
      byte[] cells = new byte[length];

      for (int index=0; index<length; index+=1) {
        char character = string.charAt(index);
        Byte dots = characterMap.get(character);
        cells[index] = (dots != null)? dots: KeyMask.DOTS_12345678;
      }

      if (writeCells(cells)) {
        return true;
      }
    }

    return false;
  }

  public static boolean write (CharSequence characters) {
    return write(characters.toString());
  }

  public static boolean write (StringBuilder sb) {
    return write(sb.toString());
  }

  public static boolean write (AccessibilityNodeInfo node, boolean describe) {
    CharSequence cs;
    StringBuilder sb = new StringBuilder();

    if (node.isCheckable()) {
      sb.append('[');
      sb.append(node.isChecked()? 'X': ' ');
      sb.append("] ");
    }

    if ((cs = node.getText()) != null) {
      sb.append(cs);
    } else if ((cs = node.getContentDescription()) != null) {
      sb.append(cs);
    } else {
      String string = node.getClassName().toString();
      int index = string.lastIndexOf('.');
      string = string.substring(index+1);

      sb.append('(');
      sb.append(string);
      sb.append(')');
    }

    if (describe) {
      sb.append(' ');
      if (node.isFocusable()) sb.append('i');
      if (node.isFocused()) sb.append('I');
      if (node.isAccessibilityFocused()) sb.append('A');
      if (node.isSelected()) sb.append('X');
      if (node.isScrollable()) sb.append('s');
    }

    return write(sb);
  }

  public static boolean write (AccessibilityNodeInfo node) {
    return write(node, false);
  }

  private BrailleDevice () {
  }

  static {
    System.loadLibrary("InputService");

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
