package org.nbp.b2g.ui;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class Keyboard {
  private final static String LOG_TAG = Keyboard.class.getName();

  private final static KeyboardInjector injector = Devices.keyboard.get();

  private static void logKeyboardEvent (int key, String action) {
    if (ApplicationSettings.LOG_ACTIONS) {
      StringBuilder sb = new StringBuilder();
      String name = getScanCodeName(key);

      sb.append("injecting scan code ");
      sb.append(action);
      sb.append(": ");
      sb.append(key);

      if (name != null) {
        sb.append(" (");
        sb.append(name);
        sb.append(')');
      }

      Log.v(LOG_TAG, sb.toString());
    }
  }

  public static boolean pressKey (int key) {
    logKeyboardEvent(key, "press");
    return injector.keyboardPress(key);
  }

  public static boolean releaseKey (int key) {
    logKeyboardEvent(key, "release");
    return injector.keyboardRelease(key);
  }

  public static boolean injectKey (int key, boolean press) {
    return press? pressKey(key): releaseKey(key);
  }

  public static boolean injectKey (int key, long duration) {
    if (pressKey(key)) {
      ApplicationUtilities.sleep(duration);

      if (releaseKey(key)) {
        return true;
      }
    }

    return false;
  }

  public static boolean injectKey (int key) {
    return injectKey(key, 0);
  }

  public final static int NULL_SCAN_CODE = 0;
  private static Map<String, Integer> scanCodeMap_nameToValue = new HashMap<String, Integer>();
  private static Map<Integer, String> scanCodeMap_valueToName = new HashMap<Integer, String>();

  public static Collection<Integer> getScanCodeValues () {
    return scanCodeMap_nameToValue.values();
  }

  public static int getScanCodeValue (String name) {
    Integer value = scanCodeMap_nameToValue.get(name);
    if (value != null) return value;

    Log.w(LOG_TAG, "keyboard scan code not defined: " + name);
    return NULL_SCAN_CODE;
  }

  public static String getScanCodeName (int value) {
    String name = scanCodeMap_valueToName.get(value);
    if (name != null) return name;

    Log.w(LOG_TAG, "keyboard scan code not defined: " + value);
    return null;
  }

  private Keyboard () {
  }

  private static void defineScanCode (String name, int code) {
    Integer value = code;
    scanCodeMap_nameToValue.put(name, value);
    scanCodeMap_valueToName.put(value, name);
  }

  private static native void defineScanCodes ();

  static {
    ApplicationUtilities.loadLibrary();
    defineScanCodes();
  }
}
