package org.nbp.b2g.ui;

import android.util.Log;

import android.view.MotionEvent;
import android.view.InputDevice;
import android.app.Instrumentation;

public class MotionDevice implements GestureInjector {
  private final static String LOG_TAG = MotionDevice.class.getName();

  private final Instrumentation instrumentation = new Instrumentation();

  private int pointerCount;
  private MotionEvent.PointerProperties[] pointerProperties;
  private MotionEvent.PointerCoords[] pointerCoordinates;

  private int deviceIdentifier;
  private int metaState;
  private int buttonState;
  private float xPrecision;
  private float yPrecision;
  private int edgeFlags;
  private int eventFlags;
  private long startTime;

  private void clearFields () {
    pointerCount = 0;
    pointerProperties = null;
    pointerCoordinates = null;

    deviceIdentifier = 0;
    metaState = 0;
    buttonState = 0;
    xPrecision = 1.0f;
    yPrecision = 1.0f;
    edgeFlags = 0;
    eventFlags = 0;
    startTime = 0;
  }

  private void setFields (int fingers, int x, int y) {
    pointerProperties = new MotionEvent.PointerProperties[fingers];
    pointerCoordinates = new MotionEvent.PointerCoords[fingers];

    for (int index=0; index<fingers; index+=1) {
      MotionEvent.PointerProperties properties = new MotionEvent.PointerProperties();
      pointerProperties[index] = properties;

      properties.id = index;
    }

    for (int index=0; index<fingers; index+=1) {
      MotionEvent.PointerCoords coordinates = new MotionEvent.PointerCoords();
      pointerCoordinates[index] = coordinates;

      coordinates.x = (float)x;
      coordinates.y = (float)y;
      coordinates.pressure = 1.0f;
      coordinates.size = 1.0f;

      int increment = (index + 1) / 2;
      if ((index % 2) == 1) increment = -increment;
      coordinates.x += (float)increment;
    }

    startTime = ApplicationUtilities.getSystemClock();
  }

  private boolean injectEvent (MotionEvent event) {
    instrumentation.sendPointerSync(event);
    return true;
  }

  private boolean injectEvent (int action) {
    long now = ApplicationUtilities.getSystemClock();

    MotionEvent event = MotionEvent.obtain(
      startTime, now, action,
      pointerCount, pointerProperties, pointerCoordinates,
      metaState, buttonState, xPrecision, yPrecision, deviceIdentifier,
      edgeFlags, InputDevice.SOURCE_TOUCHSCREEN, eventFlags
    );

    boolean injected = injectEvent(event);
    event.recycle();
    return injected;
  }

  private int makePointerAction (int action) {
    return action | ((pointerCount - 1) << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
  }

  @Override
  public boolean gestureBegin (int x, int y, int fingers) {
    Log.v(LOG_TAG, String.format(
      "motion event: begin [%d,%d] Fingers:%d", x, y, fingers
    ));

    setFields(fingers, x, y);

    pointerCount += 1;
    if (!injectEvent(MotionEvent.ACTION_DOWN)) return false;

    while (pointerCount < fingers) {
      pointerCount += 1;
      if (!injectEvent(makePointerAction(MotionEvent.ACTION_POINTER_DOWN))) return false;
    }

    return true;
  }

  @Override
  public boolean gestureEnd () {
    Log.v(LOG_TAG, "motion event: end");

    while (pointerCount > 1) {
      if (!injectEvent(makePointerAction(MotionEvent.ACTION_POINTER_UP))) break;
      pointerCount -= 1;
    }

    if (pointerCount == 1) {
      if (injectEvent(MotionEvent.ACTION_UP)) {
        pointerCount -= 1;
      }
    }

    boolean ok = pointerCount == 0;
    clearFields();
    return ok;
  }

  @Override
  public boolean gestureMove (int x, int y) {
    Log.v(LOG_TAG, String.format(
      "motion event: move [%d,%d]", x, y
    ));

    float xIncrement;
    float yIncrement;

    {
      MotionEvent.PointerCoords coordinates = pointerCoordinates[0];
      xIncrement = (float)x - coordinates.x;
      yIncrement = (float)y - coordinates.y;
    }

    for (MotionEvent.PointerCoords coordinates : pointerCoordinates) {
      coordinates.x += xIncrement;
      coordinates.y += yIncrement;
    }

    return injectEvent(MotionEvent.ACTION_MOVE);
  }

  public MotionDevice () {
    clearFields();
  }
}
