package org.nbp.b2g.ui;

import java.util.BitSet;
import java.util.Map;
import java.util.LinkedHashMap;

import org.nbp.common.Braille;

import android.util.Log;

public class KeySet extends BitSet {
  private final static String LOG_TAG = KeySet.class.getName();

  public KeySet () {
    super();
  }

  public final void set (KeySet keys) {
    clear();
    int count = keys.length();

    for (int index=0; index<count; index+=1) {
      if (keys.get(index)) set(index);
    }
  }

  private static String normalizeKeyName (String name) {
    return name.toLowerCase();
  }

  private static class KeyEntry {
    public final String name;
    public final int index;

    public KeyEntry (String name, int index) {
      this.name = name;
      this.index = index;
    }
  }

  private final static Map<String, KeyEntry> keyEntries =
         new LinkedHashMap<String, KeyEntry>();

  private static int addKey (String name) {
    synchronized (keyEntries) {
      int index = keyEntries.size();
      keyEntries.put(normalizeKeyName(name), new KeyEntry(name, index));
      return index;
    }
  }

  public final static int PAN_FORWARD  = addKey("Forward");
  public final static int PAN_BACKWARD = addKey("Backward");
  public final static int SPACE        = addKey("Space");

  public final static int PAD_UP       = addKey("Up");
  public final static int PAD_DOWN     = addKey("Down");
  public final static int PAD_LEFT     = addKey("Left");
  public final static int PAD_RIGHT    = addKey("Right");
  public final static int PAD_CENTER   = addKey("Center");

  public final static int DOT_1        = addKey("Dot1");
  public final static int DOT_2        = addKey("Dot2");
  public final static int DOT_3        = addKey("Dot3");
  public final static int DOT_4        = addKey("Dot4");
  public final static int DOT_5        = addKey("Dot5");
  public final static int DOT_6        = addKey("Dot6");
  public final static int DOT_7        = addKey("Dot7");
  public final static int DOT_8        = addKey("Dot8");

  public final static int VOLUME_DOWN  = addKey("VolumeDown");
  public final static int VOLUME_UP    = addKey("VolumeUp");

  public final static int CURSOR       = addKey("Cursor");
  public final static int LONG_PRESS   = addKey("LongPress");

  protected final static KeySet panKeys = new KeySet() {
    {
      set(PAN_FORWARD);
      set(PAN_BACKWARD);
    }
  };

  public static boolean isPanKey (int index) {
    return panKeys.get(index);
  }

  protected final static KeySet padKeys = new KeySet() {
    {
      set(PAD_UP);
      set(PAD_DOWN);
      set(PAD_LEFT);
      set(PAD_RIGHT);
      set(PAD_CENTER);
    }
  };

  public static boolean isPadKey (int index) {
    return padKeys.get(index);
  }

  protected final static KeySet dotKeys = new KeySet() {
    {
      set(DOT_1);
      set(DOT_2);
      set(DOT_3);
      set(DOT_4);
      set(DOT_5);
      set(DOT_6);
      set(DOT_7);
      set(DOT_8);
    }
  };

  public static boolean isDotKey (int index) {
    return dotKeys.get(index);
  }

  protected final static KeySet volumeKeys = new KeySet() {
    {
      set(VOLUME_DOWN);
      set(VOLUME_UP);
    }
  };

  public static boolean isVolumeKey (int index) {
    return volumeKeys.get(index);
  }

  private final static Map<Integer, Byte> keyDots =
         new LinkedHashMap<Integer, Byte>()
  {
    {
      put(DOT_1, Braille.CELL_DOT_1);
      put(DOT_2, Braille.CELL_DOT_2);
      put(DOT_3, Braille.CELL_DOT_3);
      put(DOT_4, Braille.CELL_DOT_4);
      put(DOT_5, Braille.CELL_DOT_5);
      put(DOT_6, Braille.CELL_DOT_6);
      put(DOT_7, Braille.CELL_DOT_7);
      put(DOT_8, Braille.CELL_DOT_8);
    }
  };

  public final Byte toDots () {
    byte dots = 0;
    boolean space = false;
    int count = length();

    for (int index=0; index<count; index+=1) {
      if (get(index)) {
        if (index == SPACE) {
          space = true;
        } else {
          Byte dot = keyDots.get(index);
          if (dot == null) return null;
          dots |= dot;
        }
      }
    }

    if (space == (dots != 0)) return null;
    return dots;
  }

  public final boolean isDots () {
    return toDots() != null;
  }

  public static KeySet fromDotNumbers (String numbers) {
    if (numbers.isEmpty()) {
      Log.w(LOG_TAG, "missing dot number(s)");
      return null;
    }

    KeySet keys = new KeySet();
    int length = numbers.length();

    for (int index=0; index<length; index+=1) {
      final char number = numbers.charAt(index);
      final int key;

      switch (number) {
        case '1': key = DOT_1; break;
        case '2': key = DOT_2; break;
        case '3': key = DOT_3; break;
        case '4': key = DOT_4; break;
        case '5': key = DOT_5; break;
        case '6': key = DOT_6; break;
        case '7': key = DOT_7; break;
        case '8': key = DOT_8; break;

        default:
          Log.w(LOG_TAG, ("unknown dot number: " + number));
          return null;
      }

      if (keys.get(key)) {
        Log.w(LOG_TAG, ("dot number specified more than once: " + number));
        return null;
      }

      keys.set(key);
    }

    return keys;
  }

  public static KeySet fromName (String name) {
    name = normalizeKeyName(name);

    {
      KeyEntry key = keyEntries.get(name);

      if (key != null) {
        KeySet keys = new KeySet();
        keys.set(key.index);
        return keys;
      }
    }

    {
      String prefix = "dots";

      if (name.startsWith(prefix)) {
        KeySet keys = fromDotNumbers(name.substring(prefix.length()));
        if (keys != null) return keys;
      }
    }

    Log.w(LOG_TAG, ("unknown key name: " + name));
    return null;
  }

  public final String toString () {
    StringBuilder sb = new StringBuilder();

    if (!isEmpty()) {
      KeySet keys = (KeySet)clone();
      int dotCount = 0;

      for (KeyEntry key : keyEntries.values()) {
        if (keys.get(key.index)) {
          String name = key.name;
          boolean isDot = isDotKey(key.index);

          if (!(isDot && (dotCount > 0))) {
            if (sb.length() > 0) sb.append(KeyBindings.KEY_NAME_DELIMITER);
            sb.append(name);
          } else {
            if (dotCount == 1) sb.insert((sb.length() - 1), 's');
            sb.append(name.charAt(name.length() - 1));
          }

          keys.clear(key.index);
          if (keys.isEmpty()) break;

          dotCount = isDot? (dotCount + 1): 0;
        }
      }

      if (!keys.isEmpty()) {
        int count = keys.length();

        for (int index=0; index<count; index+=1) {
          if (keys.get(index)) {
            if (sb.length() > 0) sb.append(KeyBindings.KEY_NAME_DELIMITER);
            sb.append(String.format("key#%d", index));
          }
        }
      }
    }

    return sb.toString();
  }
}
