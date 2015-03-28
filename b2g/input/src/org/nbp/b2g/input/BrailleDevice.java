package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

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

  public static boolean writeCells (String string) {
    int length = string.length();
    byte[] cells = new byte[length];

    for (int index=0; index<length; index+=1) {
      char character = string.charAt(index);
      Byte dots = characterMap.get(character);
      cells[index] = (dots != null)? dots: KeyMask.DOTS_12345678;
    }

    return writeCells(cells);
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
    }
  }
}
