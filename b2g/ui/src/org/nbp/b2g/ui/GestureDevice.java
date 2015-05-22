package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public abstract class GestureDevice extends UInputDevice {
  private final static String LOG_TAG = GestureDevice.class.getName();

  protected abstract boolean gestureEnable (ByteBuffer uinput, int width, int height);
  protected abstract boolean gestureBegin (ByteBuffer uinput, int x, int y);
  protected abstract boolean gestureEnd (ByteBuffer uinput);
  protected abstract boolean gestureLocation (ByteBuffer uinput, int x, int y);

  public boolean tap (int x, int y, int count) {
    if (count < 1) return false;
    if (ApplicationContext.isTouchExplorationActive()) count += 1;

    if (!open()) return false;
    ByteBuffer uinput = getUInputDescriptor();

    while (true) {
      if (!gestureBegin(uinput, x, y)) return false;
      ApplicationUtilities.sleep(45);
      if (!gestureEnd(uinput)) return false;
      if ((count -= 1) == 0) return true;
      ApplicationUtilities.sleep(100);
    }
  }

  public boolean tap (int x, int y) {
    return tap(x, y, 1);
  }

  public boolean swipe (int x1, int y1, int x2, int y2) {
    if (open()) {
      ByteBuffer uinput = getUInputDescriptor();

      if (gestureBegin(uinput, x1, y1)) {
        if (gestureLocation(uinput, x2, y2)) {
          if (gestureEnd(uinput)) {
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
      Log.d(LOG_TAG, String.format("screen size: %dx%d", width, height));
    } else {
      width = 0;
      height = 0;
    }

    return gestureEnable(uinput, width, height);
  }

  public GestureDevice () {
    super();
  }
}
