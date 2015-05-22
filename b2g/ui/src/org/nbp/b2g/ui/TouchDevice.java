package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public class TouchDevice extends GestureDevice {
  private final static String LOG_TAG = TouchDevice.class.getName();

  protected native boolean enableTouchEvents (ByteBuffer uinput, int width, int height);
  protected native boolean gestureBegin (ByteBuffer uinput, int x, int y);
  protected native boolean gestureEnd (ByteBuffer uinput);
  protected native boolean gestureLocation (ByteBuffer uinput, int x, int y);

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

    return enableTouchEvents(uinput, width, height);
  }

  public TouchDevice () {
    super();
  }
}
