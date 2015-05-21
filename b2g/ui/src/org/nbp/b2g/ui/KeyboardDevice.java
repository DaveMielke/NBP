package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class KeyboardDevice extends UInputDevice {
  private final static String LOG_TAG = KeyboardDevice.class.getName();

  protected native boolean enableKeyEvents (ByteBuffer uinput);
  protected native boolean enableKey (ByteBuffer uinput, int key);
  protected native boolean pressKey (ByteBuffer uinput, int key);
  protected native boolean releaseKey (ByteBuffer uinput, int key);

  public boolean injectKeyPress (int key) {
    if (open()) {
      if (pressKey(getUInputDescriptor(), key)) {
        return true;
      }
    }

    return false;
  }

  public boolean injectKeyRelease (int key) {
    if (open()) {
      if (releaseKey(getUInputDescriptor(), key)) {
        return true;
      }
    }

    return false;
  }

  public boolean injectKey (int key, boolean press) {
    return press? injectKeyPress(key): injectKeyRelease(key);
  }

  public boolean injectKey (int key, long duration) {
    if (injectKey(key, true)) {
      ApplicationUtilities.sleep(duration);

      if (injectKey(key, false)) {
        return true;
      }
    }

    return false;
  }

  public boolean injectKey (int key) {
    return injectKey(key, 0);
  }

  public final static int NULL_SCAN_CODE = 0;
  private static Map<String, Integer> scanCodeMap = new HashMap<String, Integer>();

  private boolean enableKeys (ByteBuffer uinput) {
    for (int key : scanCodeMap.values()) {
      if (!enableKey(uinput, key)) return false;
    }

    return true;
  }

  @Override
  protected boolean prepareDevice (ByteBuffer uinput) {
    if (enableKeyEvents(uinput)) {
      if (enableKeys(uinput)) {
        return true;
      }
    }

    return false;
  }

  public KeyboardDevice () {
    super();
  }

  public static int getScanCode (String name) {
    Integer code = scanCodeMap.get(name);
    if (code != null) return code;
    Log.w(LOG_TAG, "keyboard scan code not defined: " + name);
    return NULL_SCAN_CODE;
  }

  private static void defineScanCode (String name, int code) {
    scanCodeMap.put(name, code);
  }

  private static native void defineScanCodes ();

  static {
    System.loadLibrary("UserInterface");
    defineScanCodes();
  }
}
