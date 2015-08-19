package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

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

  private boolean enableKeys (ByteBuffer uinput) {
    for (int key : Keyboard.getScanCodeValues()) {
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

  static {
    ApplicationUtilities.loadLibrary();
  }
}
