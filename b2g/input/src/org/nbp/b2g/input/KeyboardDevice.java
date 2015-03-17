package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class KeyboardDevice extends UInputDevice {
  private static final String LOG_TAG = KeyboardDevice.class.getName();

  public final static int NO_SCAN_CODE = -1;

  private static Map<String, Integer> scanCodeMap = new HashMap<String, Integer>();

  private boolean enableKeys (int device) {
    for (int key : scanCodeMap.values()) {
      if (!enableKey(device, key)) return false;
    }

    return true;
  }

  @Override
  protected boolean prepareDevice (int device) {
    if (enableKeyEvents(device)) {
      if (enableKeys(device)) {
        return true;
      }
    }

    return false;
  }

  public boolean press () {
    return pressKey(0);
  }

  public boolean release () {
    return releaseKey(0);
  }

  public KeyboardDevice () {
    super();
  }

  public static int getScanCode (String name) {
    Integer code = scanCodeMap.get(name);
    if (code != null) return code;
    Log.w(LOG_TAG, "keyboard scan code not defined: " + name);
    return NO_SCAN_CODE;
  }

  private static void defineScanCode (String name, int code) {
    scanCodeMap.put(name, new Integer(code));
  }

  private static native void defineScanCodes ();

  static {
    System.loadLibrary("InputService");
    defineScanCodes();
  }
}
