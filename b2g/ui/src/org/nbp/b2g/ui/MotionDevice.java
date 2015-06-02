package org.nbp.b2g.ui;

import android.util.Log;

import android.view.MotionEvent;
import android.view.InputDevice;
import android.app.Instrumentation;

public class MotionDevice implements GestureInjector {
  private final static String LOG_TAG = MotionDevice.class.getName();

  private final Instrumentation instrumentation = new Instrumentation();

  private long startTime;
  private int lastX;
  private int lastY;

  private void resetFields () {
    startTime = 0;
    lastX = 0;
    lastY = 0;
  }

  private boolean injectEvent (MotionEvent event) {
    instrumentation.sendPointerSync(event);
    return true;
  }

  private boolean injectEvent (int action, int x, int y) {
    long now = ApplicationUtilities.getSystemClock();
    MotionEvent event = MotionEvent.obtain(startTime, now, action, x, y, 0);
    event.setSource(InputDevice.SOURCE_TOUCHSCREEN);

    boolean injected = injectEvent(event);
    event.recycle();

    if (injected) {
      lastX = x;
      lastY = y;
    }

    return injected;
  }

  @Override
  public boolean gestureBegin (int x, int y, int fingers) {
    Log.v(LOG_TAG, String.format(
      "motion event: begin [%d,%d] Fingers:%d", x, y, fingers
    ));

    startTime = ApplicationUtilities.getSystemClock();
    return injectEvent(MotionEvent.ACTION_DOWN, x, y);
  }

  @Override
  public boolean gestureEnd () {
    Log.v(LOG_TAG, "motion event: end");
    boolean injected = injectEvent(MotionEvent.ACTION_UP, lastX, lastY);

    resetFields();
    return injected;
  }

  @Override
  public boolean gestureMove (int x, int y) {
    Log.v(LOG_TAG, String.format(
      "motion event: move [%d,%d]", x, y
    ));

    return injectEvent(MotionEvent.ACTION_MOVE, x, y);
  }

  public MotionDevice () {
    resetFields();
  }
}
