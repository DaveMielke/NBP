package org.nbp.b2g.ui;

import java.util.Map;
import java.util.LinkedHashMap;

import android.util.Log;

public abstract class KeyMask {
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
  public final static int WAKE        = 0X040000;
  public final static int SLEEP       = 0X080000;
  public final static int CURSOR      = 0X100000;
  public final static int LONG_PRESS  = 0X200000;

  public final static int DOTS_ALL = (DOT_1 | DOT_2 | DOT_3 | DOT_4 | DOT_5 | DOT_6 | DOT_7 | DOT_8);
  public final static char KEY_COMBINATION_DELIMITER = ',';
  public final static char KEY_NAME_DELIMITER = '+';

  public static Byte toDots (int mask) {
    if (mask == 0) return null;
    if (mask == SPACE) return 0;
    if ((mask & ~DOTS_ALL) != 0) return null;

    byte dots = 0;
    if ((mask & DOT_1) != 0) dots |= BrailleDevice.DOT_1;
    if ((mask & DOT_2) != 0) dots |= BrailleDevice.DOT_2;
    if ((mask & DOT_3) != 0) dots |= BrailleDevice.DOT_3;
    if ((mask & DOT_4) != 0) dots |= BrailleDevice.DOT_4;
    if ((mask & DOT_5) != 0) dots |= BrailleDevice.DOT_5;
    if ((mask & DOT_6) != 0) dots |= BrailleDevice.DOT_6;
    if ((mask & DOT_7) != 0) dots |= BrailleDevice.DOT_7;
    if ((mask & DOT_8) != 0) dots |= BrailleDevice.DOT_8;
    return dots;
  }

  public static boolean isDots (int mask) {
    return toDots(mask) != null;
  }

  private static class KeyEntry {
    public final String name;
    public final int bit;

    public KeyEntry (String name, int bit) {
      this.name = name;
      this.bit = bit;
    }
  }

  private static Map<String, KeyEntry> keyEntries = new LinkedHashMap<String, KeyEntry>();

  private static String normalizeKeyName (String name) {
    return name.toLowerCase();
  }

  public static Integer parseDots (String numbers) {
    if (numbers.isEmpty()) {
      Log.w(LOG_TAG, "missing dot numbers");
      return null;
    }

    int mask = 0;
    int length = numbers.length();

    for (int index=0; index<length; index+=1) {
      char number = numbers.charAt(index);
      int bit;

      switch (number) {
        case '1': bit = KeyMask.DOT_1; break;
        case '2': bit = KeyMask.DOT_2; break;
        case '3': bit = KeyMask.DOT_3; break;
        case '4': bit = KeyMask.DOT_4; break;
        case '5': bit = KeyMask.DOT_5; break;
        case '6': bit = KeyMask.DOT_6; break;
        case '7': bit = KeyMask.DOT_7; break;
        case '8': bit = KeyMask.DOT_8; break;

        default:
          Log.w(LOG_TAG, "unknown dot number: " + number);
          return null;
      }

      if ((mask & bit) != 0) {
        Log.w(LOG_TAG, "dot number specified more than once: " + number);
        return null;
      }

      mask |= bit;
    }

    return mask;
  }

  public static Integer toBit (String name) {
    name = normalizeKeyName(name);

    {
      KeyEntry key = keyEntries.get(name);
      if (key != null) return key.bit;
    }

    {
      String prefix = "dots";

      if (name.startsWith(prefix)) {
        String numbers = name.substring(prefix.length());
        Integer mask = parseDots(numbers);
        if (mask != null) return mask;
      }
    }

    Log.w(LOG_TAG, "unknown key name: " + name);
    return null;
  }

  public static String toString (int mask) {
    StringBuilder sb = new StringBuilder();

    if (mask != 0) {
      for (KeyEntry key : keyEntries.values()) {
        if ((mask & key.bit) != 0) {
          if (sb.length() > 0) sb.append(KEY_NAME_DELIMITER);
          sb.append(key.name);
          if ((mask &= ~key.bit) == 0) break;
        }
      }

      if (mask != 0) {
        if (sb.length() > 0) sb.append(KEY_NAME_DELIMITER);
        sb.append(String.format("0X%X", mask));
      }
    }

    return sb.toString();
  }

  private static void map (String name, int bit) {
    keyEntries.put(normalizeKeyName(name), new KeyEntry(name, bit));
  }

  static {
    map("Forward", FORWARD);
    map("Backward", BACKWARD);
    map("Space", SPACE);

    map("Up", DPAD_UP);
    map("Down", DPAD_DOWN);
    map("Left", DPAD_LEFT);
    map("Right", DPAD_RIGHT);
    map("Center", DPAD_CENTER);

    map("Dot1", DOT_1);
    map("Dot2", DOT_2);
    map("Dot3", DOT_3);
    map("Dot4", DOT_4);
    map("Dot5", DOT_5);
    map("Dot6", DOT_6);
    map("Dot7", DOT_7);
    map("Dot8", DOT_8);

    map("VolumeDown", VOLUME_DOWN);
    map("VolumeUp", VOLUME_UP);

    map("Cursor", CURSOR);
    map("LongPress", LONG_PRESS);

    map("Wake", WAKE);
    map("Sleep", SLEEP);
  }

  private KeyMask () {
  }
}
