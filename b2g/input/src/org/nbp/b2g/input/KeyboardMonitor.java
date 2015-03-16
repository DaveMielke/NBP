package org.nbp.b2g.input;

import android.util.Log;

public class KeyboardMonitor extends Thread {
  private static final String LOG_TAG = KeyboardMonitor.class.getName();

  private static native boolean openKeyboard ();
  private static native void closeKeyboard ();
  private static native void monitorKeyboard ();

  public void onKeyEvent (int code, boolean press) {
    Log.d(LOG_TAG, "key " + (press? "press": "release") + ": " + code);
  }

  public void run () {
    Log.d(LOG_TAG, "keyboard monitor starting");
    monitorKeyboard();
    Log.d(LOG_TAG, "keyboard monitor stopping");
  }

  public static boolean startMonitor () {
    if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      if (openKeyboard()) {
        new KeyboardMonitor().start();
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
