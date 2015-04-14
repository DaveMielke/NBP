package org.nbp.b2g.ui;

import android.util.Log;

public abstract class EventMonitor extends Thread {
  private final static String LOG_TAG = EventMonitor.class.getName();

  public final static KeyboardDevice keyboardDevice = new KeyboardDevice();

  protected abstract boolean isEnabled ();
  protected abstract int openDevice ();
  private native void closeDevice (int device);
  private native void monitorDevice (int device);

  public void onKeyEvent (int code, boolean press) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "scan code " + (press? "press": "release") + " received: " + code);
    }

    if ((code >= ScanCode.CURSOR_0) && (code <= ScanCode.CURSOR_19)) {
      KeyEvents.handleCursorKeyEvent((code - ScanCode.CURSOR_0), press);
    } else {
      KeyEvents.handleNavigationKeyEvent(ScanCode.toKeyMask(code), press);
    }
  }

  public void run () {
    String name = getName();
    Log.d(LOG_TAG, name + " started");

    final int NO_DEVICE = -1;
    int device = openDevice();

    if (device != NO_DEVICE) {
      monitorDevice(device);
      closeDevice(device);
    } else {
      Log.w(LOG_TAG, name + " device not opened");
    }

    Log.d(LOG_TAG, name + " stopped");
  }

  public EventMonitor (String threadName) {
    super(threadName);
  }

  static {
    System.loadLibrary("UserInterface");
  }
}
