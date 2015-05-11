package org.nbp.b2g.ui;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class KeyMask {
  private final static String LOG_TAG = KeyMask.class.getName();

  public final static int DOT_1       = 0X000001;
  public final static int DOT_2       = 0X000002;
  public final static int DOT_3       = 0X000004;
  public final static int DOT_4       = 0X000008;
  public final static int DOT_5       = 0X000010;
  public final static int DOT_6       = 0X000020;
  public final static int DOT_7       = 0X000040;
  public final static int DOT_8       = 0X000080;
  public final static int SPACE       = 0X000100;
  public final static int FORWARD     = 0X000200;
  public final static int BACKWARD    = 0X000400;
  public final static int DPAD_CENTER = 0X000800;
  public final static int DPAD_LEFT   = 0X001000;
  public final static int DPAD_RIGHT  = 0X002000;
  public final static int DPAD_UP     = 0X004000;
  public final static int DPAD_DOWN   = 0X008000;
  public final static int VOLUME_DOWN = 0X010000;
  public final static int VOLUME_UP   = 0X020000;
  public final static int POWER_ON    = 0X040000;
  public final static int POWER_OFF   = 0X080000;
  public final static int CURSOR      = 0X100000;
  public final static int LONG_PRESS  = 0X200000;

  public final static int DOTS_ALL = (DOT_1 | DOT_2 | DOT_3 | DOT_4 | DOT_5 | DOT_6 | DOT_7 | DOT_8);

  private static Map<Character, Integer> charToBit = new HashMap<Character, Integer>();

  public static Integer charToBit (char character) {
    Integer bit = charToBit.get(character);

    if (bit == null) {
      Log.w(LOG_TAG, "key mask character not defined: " + character);
    }

    return bit;
  }

  private static void map (char character, int bit) {
    charToBit.put(character, bit);
  }

  private static void map () {
    map('1', DOT_1);
    map('2', DOT_2);
    map('3', DOT_3);
    map('4', DOT_4);
    map('5', DOT_5);
    map('6', DOT_6);
    map('7', DOT_7);
    map('8', DOT_8);

    map('S', SPACE);
    map('F', FORWARD);
    map('B', BACKWARD);

    map('C', DPAD_CENTER);
    map('U', DPAD_UP);
    map('D', DPAD_DOWN);
    map('L', DPAD_LEFT);
    map('R', DPAD_RIGHT);

    map('<', VOLUME_DOWN);
    map('>', VOLUME_UP);

    map('X', CURSOR);
    map('H', LONG_PRESS);

    map('P', POWER_ON);
  }

  static {
    map();
  }
}
