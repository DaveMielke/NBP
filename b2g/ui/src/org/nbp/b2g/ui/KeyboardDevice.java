package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class KeyboardDevice extends UInputDevice implements KeyboardInjector {
  private final static String LOG_TAG = KeyboardDevice.class.getName();

  protected native boolean keyboardEnable (ByteBuffer uinput);
  protected native boolean keyEnable (ByteBuffer uinput, int key);

  protected native boolean keyboardPress (ByteBuffer uinput, int key);
  protected native boolean keyboardRelease (ByteBuffer uinput, int key);

  @Override
  public boolean keyboardPress (int key) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return keyboardPress(uinput, key);
  }

  @Override
  public boolean keyboardRelease (int key) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return keyboardRelease(uinput, key);
  }

  public final static int NULL_SCAN_CODE = 0;
  private static Map<String, Integer> scanCodeMap = new HashMap<String, Integer>();

  private boolean enableKeys (ByteBuffer uinput) {
    for (int key : scanCodeMap.values()) {
      if (!keyEnable(uinput, key)) return false;
    }

    return true;
  }

  @Override
  protected boolean prepareDevice (ByteBuffer uinput) {
    if (keyboardEnable(uinput)) {
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
    defineScanCodes();
  }
}
