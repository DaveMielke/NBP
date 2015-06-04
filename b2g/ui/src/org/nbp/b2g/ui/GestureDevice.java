package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.graphics.Point;

import android.util.Log;

public abstract class GestureDevice extends UInputDevice implements GestureInjector {
  private final static String LOG_TAG = GestureDevice.class.getName();

  protected abstract boolean gestureBegin (ByteBuffer uinput, int x, int y, int fingers);
  protected abstract boolean gestureMove (ByteBuffer uinput, int x, int y);
  protected abstract boolean gestureEnd (ByteBuffer uinput);

  @Override
  public boolean gestureEnabled () {
    return getUInputDescriptor() != null;
  }

  @Override
  public boolean gestureBegin (int x, int y, int fingers) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureBegin(uinput, x, y, fingers);
  }

  @Override
  public boolean gestureMove (int x, int y) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureMove(uinput, x, y);
  }

  @Override
  public boolean gestureEnd () {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureEnd(uinput);
  }

  @Override
  public boolean gestureEnd (int x, int y) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;

    if (!gestureMove(uinput, x, y)) return false;
    return gestureEnd(uinput);
  }

  protected Point getScreenSize () {
    Point size = ApplicationContext.getScreenSize();
    if (size == null) size = new Point(0, 0);
    Log.d(LOG_TAG, String.format("screen size: %dx%d", size.x, size.y));
    return size;
  }

  public GestureDevice () {
    super();
  }
}
