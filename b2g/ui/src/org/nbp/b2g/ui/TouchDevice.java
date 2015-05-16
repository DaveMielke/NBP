package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public class TouchDevice extends UInputDevice {
  private final static String LOG_TAG = TouchDevice.class.getName();

  protected native boolean enableTouchEvents (ByteBuffer uinput, int width, int height);
  protected native boolean touchBegin (ByteBuffer uinput, int x, int y);
  protected native boolean touchEnd (ByteBuffer uinput);
  protected native boolean touchLocation (ByteBuffer uinput, int x, int y);

  public boolean tapScreen (int x, int y) {
    if (open()) {
      ByteBuffer uinput = getUInputDescriptor();

      if (touchBegin(uinput, x, y)) {
        ApplicationUtilities.sleep(100);

        if (touchEnd(uinput)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean tapScreen (int x, int y, int count) {
    if (count < 1) return false;

    while (true) {
      if (!tapScreen(x, y)) return false;
      if (--count < 1) return true;
      ApplicationUtilities.sleep(200);
    }
  }

  public boolean swipeScreen (int x1, int y1, int x2, int y2) {
    if (open()) {
      ByteBuffer uinput = getUInputDescriptor();

      if (touchBegin(uinput, x1, y1)) {
        if (touchLocation(uinput, x2, y2)) {
          if (touchEnd(uinput)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  protected boolean prepareDevice (ByteBuffer uinput) {
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

    return enableTouchEvents(uinput, width, height);
  }

  public TouchDevice () {
    super();
  }

  static {
    System.loadLibrary("UserInterface");
  }
}
