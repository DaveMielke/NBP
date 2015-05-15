package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public class TouchDevice extends UInputDevice {
  private final static String LOG_TAG = TouchDevice.class.getName();

  protected native boolean enableTouchEvents (ByteBuffer device, int width, int height);
  protected native boolean touchBegin (ByteBuffer device, int x, int y);
  protected native boolean touchEnd (ByteBuffer device);
  protected native boolean touchLocation (ByteBuffer device, int x, int y);

  public boolean tapScreen (int x, int y) {
    if (open()) {
      ByteBuffer device = getDevice();

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
      ByteBuffer device = getDevice();

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
  protected boolean prepareDevice (ByteBuffer device) {
    Point size = ApplicationContext.getScreenSize();
    int width;
    int height;

    if (size != null) {
      width = size.x;
      height = size.y;
    } else {
      width = 0;
      height = 0;
    }

    return enableTouchEvents(device, width, height);
  }

  public TouchDevice () {
    super();
  }

  static {
    System.loadLibrary("UserInterface");
  }
}
