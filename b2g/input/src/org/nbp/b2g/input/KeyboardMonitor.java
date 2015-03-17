package org.nbp.b2g.input;

import android.util.Log;

public class KeyboardMonitor extends Thread {
  private static final String LOG_TAG = KeyboardMonitor.class.getName();

  private static KeyboardMonitor keyboardMonitor = null;

  public static KeyboardMonitor getKeyboardMonitor () {
    return keyboardMonitor;
  }

  public static boolean isActive () {
    KeyboardMonitor monitor = getKeyboardMonitor();

    return (monitor != null)? monitor.isAlive(): false;
  }

  private static native boolean openKeyboard ();
  private static native void closeKeyboard ();
  private static native void monitorKeyboard (KeyboardMonitor monitor);

  public void onKeyEvent (int code, boolean press) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "key " + (press? "press": "release") + ": " + code);
    }

    if (press) {
      Actions.handleKeyDown(ScanCode.toKeyMask(code));
    } else {
      Actions.handleKeyUp(ScanCode.toKeyMask(code));
    }
  }

  public void run () {
    Log.d(LOG_TAG, "keyboard monitor starting");

    if (openKeyboard()) {
      Actions.resetKeys();
      monitorKeyboard(this);

      closeKeyboard();
    } else {
      Log.w(LOG_TAG, "keyboard device not opened");
    }

    Log.d(LOG_TAG, "keyboard monitor stopping");
  }

  public static boolean startMonitor () {
    if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      keyboardMonitor = new KeyboardMonitor();
      keyboardMonitor.start();
      return true;
    }

    return false;
  }

  public KeyboardMonitor () {
    super("B2G-keyboard-monitor");
  }

  static {
    System.loadLibrary("InputService");
  }
}
