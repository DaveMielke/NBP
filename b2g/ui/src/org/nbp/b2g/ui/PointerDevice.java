package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public class PointerDevice extends GestureDevice {
  private final static String LOG_TAG = PointerDevice.class.getName();

  protected native boolean pointerEnable (ByteBuffer uinput);

  @Override
  protected native boolean gestureBegin (ByteBuffer uinput, int x, int y);

  @Override
  protected native boolean gestureEnd (ByteBuffer uinput);

  @Override
  protected native boolean gestureMove (ByteBuffer uinput, int x, int y);

  @Override
  protected boolean prepareDevice (ByteBuffer uinput) {
    return pointerEnable(uinput);
  }

  public PointerDevice () {
    super();
  }
}
