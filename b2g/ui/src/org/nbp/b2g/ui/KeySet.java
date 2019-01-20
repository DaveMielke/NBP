package org.nbp.b2g.ui;

import java.util.Set;
import java.util.HashSet;

import java.util.Map;
import java.util.LinkedHashMap;

import org.nbp.common.Braille;

import android.util.Log;
import android.view.KeyEvent;

public class KeySet {
  private final static String LOG_TAG = KeySet.class.getName();

  private final Set<Integer> includedKeys = new HashSet<Integer>();
  private boolean hasBeenFrozen = false;

  @Override
  public final int hashCode () {
    return includedKeys.hashCode();
  }

  @Override
  public final boolean equals (Object object) {
    if (object == this) return true;
    if (!(object instanceof KeySet)) return false;
    return includedKeys.equals(((KeySet)object).includedKeys);
  }

  public final boolean isEmpty () {
    return includedKeys.isEmpty();
  }

  public final boolean get (Integer code) {
    return includedKeys.contains(code);
  }

  public final boolean intersects (KeySet keys) {
    for (Integer code : keys.includedKeys) {
      if (get(code)) return true;
    }

    return false;
  }

  private final void freezeCheck () {
    if (hasBeenFrozen) throw new IllegalStateException("has been frozen");
  }

  public final KeySet freeze () {
    freezeCheck();
    hasBeenFrozen = true;
    return this;
  }

  public final void clear () {
    freezeCheck();
    includedKeys.clear();
  }

  public final boolean remove (Integer code) {
    freezeCheck();
    return includedKeys.remove(code);
  }

  public final boolean add (Integer code) {
    freezeCheck();
    return includedKeys.add(code);
  }

  public final boolean or (KeySet keys) {
    freezeCheck();
    return includedKeys.addAll(keys.includedKeys);
  }

  public final void set (KeySet keys) {
    freezeCheck();
    includedKeys.clear();
    includedKeys.addAll(keys.includedKeys);
  }

  public KeySet (Integer... codes) {
    super();

    for (int code : codes) {
      add(code);
    }
  }

  private static String normalizeKeyName (String name) {
    return name.toLowerCase();
  }

  private static class KeyDefinition {
    public final int code;
    public final String name;

    public KeyDefinition (int code, String name) {
      this.code = code;
      this.name = name;
    }
  }

  private static int lastCode = KeyEvent.getMaxKeyCode();
  private final static Map<String, KeyDefinition> keyDefinitions =
         new LinkedHashMap<String, KeyDefinition>();

  private static KeyDefinition addKey (Integer code, String name) {
    synchronized (keyDefinitions) {
      KeyDefinition key = new KeyDefinition(code, name);
      keyDefinitions.put(normalizeKeyName(name), key);
      return key;
    }
  }

  private static int addKey (String name) {
    synchronized (keyDefinitions) {
      int code = ++lastCode;
      addKey(code, name);
      return code;
    }
  }

  private static String addKey (Integer code) {
    synchronized (keyDefinitions) {
      String name = KeyEvent.keyCodeToString(code);
      if ((name == null) || name.isEmpty()) name = Integer.toString(code);
      if (Character.isDigit(name.charAt(0))) name = "Key#" + name;

      addKey(code, name);
      return name;
    }
  }

  static {
    addKey(KeyEvent.KEYCODE_SHIFT_LEFT);
    addKey(KeyEvent.KEYCODE_SHIFT_RIGHT);

    addKey(KeyEvent.KEYCODE_CTRL_LEFT);
    addKey(KeyEvent.KEYCODE_CTRL_RIGHT);

    addKey(KeyEvent.KEYCODE_ALT_LEFT);
    addKey(KeyEvent.KEYCODE_ALT_RIGHT);

    addKey(KeyEvent.KEYCODE_META_LEFT);
    addKey(KeyEvent.KEYCODE_META_RIGHT);
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

  protected final static KeySet panKeys = new KeySet(
    PAN_FORWARD, PAN_BACKWARD
  ).freeze();

  public static boolean isPanCode (int code) {
    return panKeys.get(code);
  }

  protected final static KeySet padKeys = new KeySet(
    PAD_UP, PAD_DOWN,
    PAD_LEFT, PAD_RIGHT,
    PAD_CENTER
  ).freeze();

  public static boolean isPadCode (int code) {
    return padKeys.get(code);
  }

  protected final static KeySet dotKeys = new KeySet(
    DOT_1, DOT_2, DOT_3, DOT_4,
    DOT_5, DOT_6, DOT_7, DOT_8
  ).freeze();

  public static boolean isDotCode (int code) {
    return dotKeys.get(code);
  }

  protected final static KeySet volumeKeys = new KeySet(
    VOLUME_DOWN, VOLUME_UP
  ).freeze();

  public static boolean isVolumeCode (int code) {
    return volumeKeys.get(code);
  }

  private final static Map<Integer, Byte> codeToDot =
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

    for (Integer code : includedKeys) {
      if (code == SPACE) {
        space = true;
      } else {
        Byte dot = codeToDot.get(code);
        if (dot == null) return null;
        dots |= dot;
      }
    }

    if (space == (dots != 0)) return null;
    return dots;
  }

  public final boolean isDots () {
    return toDots() != null;
  }

  public static KeySet fromDots (byte dots) {
    KeySet keys = new KeySet();

    if ((dots & Braille.CELL_DOT_1) != 0) keys.add(DOT_1);
    if ((dots & Braille.CELL_DOT_2) != 0) keys.add(DOT_2);
    if ((dots & Braille.CELL_DOT_3) != 0) keys.add(DOT_3);
    if ((dots & Braille.CELL_DOT_4) != 0) keys.add(DOT_4);
    if ((dots & Braille.CELL_DOT_5) != 0) keys.add(DOT_5);
    if ((dots & Braille.CELL_DOT_6) != 0) keys.add(DOT_6);
    if ((dots & Braille.CELL_DOT_7) != 0) keys.add(DOT_7);
    if ((dots & Braille.CELL_DOT_8) != 0) keys.add(DOT_8);

    if (keys.isEmpty()) keys.add(SPACE);
    return keys;
  }

  public static KeySet fromDotNumbers (String numbers) {
    Byte dots = Braille.parseDotNumbers(numbers);
    if (dots == null) return null;
    return fromDots(dots);
  }

  public static KeySet fromName (String name) {
    name = normalizeKeyName(name);

    synchronized (keyDefinitions) {
      KeyDefinition key = keyDefinitions.get(name);
      if (key != null) return new KeySet(key.code);
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
      Set<Integer> keysLeft = new HashSet<Integer>(includedKeys);
      int dotCount = 0;

      synchronized (keyDefinitions) {
        for (KeyDefinition key : keyDefinitions.values()) {
          int code = key.code;
          if (!keysLeft.contains(code)) continue;

          String name = key.name;
          boolean isDot = isDotCode(code);

          if (!(isDot && (dotCount > 0))) {
            if (sb.length() > 0) sb.append(KeyBindings.KEY_NAME_DELIMITER);
            sb.append(name);
          } else {
            if (dotCount == 1) sb.insert((sb.length() - 1), 's');
            sb.append(name.charAt(name.length() - 1));
          }

          keysLeft.remove(code);
          if (keysLeft.isEmpty()) break;

          dotCount = isDot? (dotCount + 1): 0;
        }
      }

      if (!keysLeft.isEmpty()) {
        for (Integer code : keysLeft) {
          if (sb.length() > 0) sb.append(KeyBindings.KEY_NAME_DELIMITER);
          sb.append(addKey(code));
        }
      }
    }

    return sb.toString();
  }
}
