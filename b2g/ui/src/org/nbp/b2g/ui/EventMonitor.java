package org.nbp.b2g.ui;

import android.util.Log;

public abstract class EventMonitor extends Thread {
  private final static String LOG_TAG = EventMonitor.class.getName();

  protected abstract boolean isEnabled ();
  protected abstract int openDevice ();
  private native void closeDevice (int device);
  private native void monitorDevice (int device);

  protected boolean handleKeyEvent (int code, boolean press) {
    return false;
  }

  public void onKeyEvent (int code, boolean press) {
    if (ApplicationSettings.LOG_KEYBOARD) {
      StringBuilder sb = new StringBuilder();
      String name = Keyboard.getScanCodeName(code);

      sb.append("scan code ");
      sb.append(press? "press": "release");
      sb.append(" received: ");
      sb.append(code);

      if (name != null) {
        sb.append(" (");
        sb.append(name);
        sb.append(')');
      }

      Log.d(LOG_TAG, sb.toString());
    }

    if (!handleKeyEvent(code, press)) {
      if ((code >= ScanCode.CURSOR_0) && (code <= ScanCode.CURSOR_19)) {
        KeyEvents.handleCursorKeyEvent((code - ScanCode.CURSOR_0), press);
      } else {
        KeyEvents.handleNavigationKeyEvent(ScanCode.toKeyMask(code), press);
      }
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
