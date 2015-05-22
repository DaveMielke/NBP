package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public class TouchDevice extends GestureDevice {
  private final static String LOG_TAG = TouchDevice.class.getName();

  protected native boolean gestureEnable (ByteBuffer uinput, int width, int height);
  protected native boolean gestureBegin (ByteBuffer uinput, int x, int y);
  protected native boolean gestureEnd (ByteBuffer uinput);
  protected native boolean gestureLocation (ByteBuffer uinput, int x, int y);

  public TouchDevice () {
    super();
  }
}
