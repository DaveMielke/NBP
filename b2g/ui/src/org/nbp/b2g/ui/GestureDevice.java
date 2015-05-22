package org.nbp.b2g.ui;

import java.nio.ByteBuffer;

import android.util.Log;

public abstract class GestureDevice extends UInputDevice implements GestureInterface {
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

  public boolean tap (int x, int y, int count) {
    if (count < 1) return false;
    if (ApplicationContext.isTouchExplorationActive()) count += 1;

    ByteBuffer uinput = getUInputDescriptor();
    if (uinput == null) return false;

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
    ByteBuffer uinput = getUInputDescriptor();

    if (uinput != null) {
      if (gestureBegin(uinput, x1, y1)) {
        if (gestureMove(uinput, x2, y2)) {
          if (gestureEnd(uinput)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public GestureDevice () {
    super();
  }
}
