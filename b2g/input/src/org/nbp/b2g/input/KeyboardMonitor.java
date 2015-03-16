package org.nbp.b2g.input;

import android.util.Log;

public class KeyboardMonitor extends Thread {
  private static final String LOG_TAG = KeyboardMonitor.class.getName();

  private static KeyboardMonitor keyboardMonitor = null;

  public static KeyboardMonitor getKeyboardMonitor () {
    return keyboardMonitor;
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
    Actions.resetKeys();
    monitorKeyboard(this);
    Log.d(LOG_TAG, "keyboard monitor stopping");
  }

  public static boolean startMonitor () {
    if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      if (openKeyboard()) {
        keyboardMonitor = new KeyboardMonitor();
        keyboardMonitor.start();
        return true;
      } else {
        Log.w(LOG_TAG, "keyboard device not opened");
      }
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
