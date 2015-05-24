package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public abstract class GestureDevice extends UInputDevice implements GestureInjector {
  private final static String LOG_TAG = GestureDevice.class.getName();

  protected abstract boolean gestureBegin (ByteBuffer uinput, int x, int y);
  protected abstract boolean gestureEnd (ByteBuffer uinput);
  protected abstract boolean gestureMove (ByteBuffer uinput, int x, int y);

  @Override
  public boolean gestureBegin (int x, int y) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureBegin(uinput, x, y);
  }

  @Override
  public boolean gestureEnd () {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureEnd(uinput);
  }

  @Override
  public boolean gestureMove (int x, int y) {
    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;
    return gestureMove(uinput, x, y);
  }

  public GestureDevice () {
    super();
  }
}
