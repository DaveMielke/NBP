package org.nbp.b2g.input;

import android.util.Log;

public class KeyboardMonitor extends Thread {
  private static final String LOG_TAG = KeyboardMonitor.class.getName();

  private static KeyboardMonitor keyboardMonitor = null;
  private final static Object keyboardMonitorStartLock = new Object();

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
      KeyEvents.handleKeyDown(ScanCode.toKeyMask(code));
    } else {
      KeyEvents.handleKeyUp(ScanCode.toKeyMask(code));
    }
  }

  public void run () {
    Log.d(LOG_TAG, "keyboard monitor starting");

    if (openKeyboard()) {
      KeyEvents.resetKeys();
      monitorKeyboard(this);

      closeKeyboard();
    } else {
      Log.w(LOG_TAG, "keyboard device not opened");
    }

    Log.d(LOG_TAG, "keyboard monitor stopping");
  }

  public static boolean startKeyboardMonitor () {
    if (ApplicationParameters.START_KEYBOARD_MONITOR) {
      synchronized (keyboardMonitorStartLock) {
        if (keyboardMonitor == null) {
          keyboardMonitor = new KeyboardMonitor();
          keyboardMonitor.start();
        }
      }

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
