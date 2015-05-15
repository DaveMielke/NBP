package org.nbp.b2g.ui;

import android.util.Log;

public class TouchDevice extends UInputDevice {
  private final static String LOG_TAG = TouchDevice.class.getName();

  protected native boolean enableTouchEvents (int device);
  protected native boolean touchBegin (int device, int x, int y);
  protected native boolean touchEnd (int device);
  protected native boolean touchLocation (int device, int x, int y);

  public boolean tapScreen (int x, int y) {
    if (open()) {
      int device = getDevice();

      if (touchBegin(device, x, y)) {
        long duration = ApplicationUtilities.getTapTimeout();
        ApplicationUtilities.sleep(duration + ApplicationParameters.LONG_PRESS_DELAY);

        if (touchEnd(device)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean swipeScreen (int x1, int y1, int x2, int y2) {
    if (open()) {
      int device = getDevice();

      if (touchBegin(device, x1, y1)) {
        if (touchLocation(device, x2, y2)) {
          if (touchEnd(device)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  protected boolean prepareDevice (int device) {
    if (enableTouchEvents(device)) {
      return true;
    }

    return false;
  }

  public TouchDevice () {
    super();
  }

  static {
    System.loadLibrary("UserInterface");
  }
}
