package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public class PointerDevice extends GestureDevice {
  private final static String LOG_TAG = PointerDevice.class.getName();

  protected native boolean pointerEnable (ByteBuffer uinput, int width, int height);

  @Override
  protected native boolean gestureBegin (ByteBuffer uinput, int x, int y, int fingers);

  @Override
  protected native boolean gestureMove (ByteBuffer uinput, int x, int y);

  @Override
  protected native boolean gestureEnd (ByteBuffer uinput);

  @Override
  protected boolean prepareDevice (ByteBuffer uinput) {
    Point size = getScreenSize();
    return pointerEnable(uinput, size.x, size.y);
  }

  public PointerDevice () {
    super();
  }

  static {
    ApplicationUtilities.loadLibrary();
  }
}
