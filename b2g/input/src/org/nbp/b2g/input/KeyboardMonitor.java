package org.nbp.b2g.input;

import android.util.Log;

public class KeyboardMonitor extends Thread {
  private static final String LOG_TAG = KeyboardMonitor.class.getName();

  private native boolean openKeyboard ();
  private native void closeKeyboard ();
  private native void monitorKeyboard ();

  public void onKeyEvent (int code, boolean press) {
    Log.d(LOG_TAG, "key " + (press? "press": "release") + ": " + code);
  }

  public void run () {
    if (openKeyboard()) monitorKeyboard();
  }

  public KeyboardMonitor () {
    super("B2G-keyboard-monitor");
  }
}
