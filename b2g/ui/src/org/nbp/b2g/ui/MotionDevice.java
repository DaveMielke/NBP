package org.nbp.b2g.ui;

import android.util.Log;

import android.app.Instrumentation;

import android.content.Context;
import android.hardware.input.InputManager;
import android.view.InputEvent;

import android.view.MotionEvent;
import android.view.InputDevice;

public class MotionDevice implements GestureInjector {
  private final static String LOG_TAG = MotionDevice.class.getName();

  private final Instrumentation instrumentation = null;

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

  private long downTime;

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

    downTime = 0;
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
  }

  private static InputManager getInputManager () {
    Object service = ApplicationContext.getSystemService(Context.INPUT_SERVICE);
    if (service != null) return (InputManager)service;

    Log.w(LOG_TAG, "no input manager");
    return null;
  }

  private boolean injectEvent (MotionEvent event) {
    if (instrumentation != null) {
      instrumentation.sendPointerSync(event);
      return true;
    }

    InputManager input = getInputManager();
    if (input == null) return false;

    Integer mode = (Integer)LanguageUtilities.getInstanceField(input, "INJECT_INPUT_EVENT_MODE_ASYNC");
    if (mode == null) return false;

    Boolean injected = (Boolean)LanguageUtilities.invokeInstanceMethod(
      input, "injectInputEvent",
      new Class[] {InputEvent.class, int.class},
      event, mode
    );

    if (injected == null) return false;
    return injected;
  }

  private boolean injectEvent (int action) {
    long now = ApplicationUtilities.getSystemClock();
    if (action == MotionEvent.ACTION_DOWN) downTime = now;

    MotionEvent event = MotionEvent.obtain(
      downTime, now, action,
      pointerCount, pointerProperties, pointerCoordinates,
      metaState, buttonState, xPrecision, yPrecision, deviceIdentifier,
      edgeFlags, InputDevice.SOURCE_TOUCHSCREEN, eventFlags
    );

    if (ApplicationSettings.LOG_ACTIONS) {
      StringBuilder sb = new StringBuilder();

      sb.append("motion event:");
      sb.append(String.format(" Action:0X%04X", action));

      for (int index=0; index<pointerCount; index+=1) {
        MotionEvent.PointerCoords coordinates = pointerCoordinates[index];
        sb.append(" [");
        sb.append(Math.round(coordinates.x));
        sb.append(',');
        sb.append(Math.round(coordinates.y));
        sb.append(']');
      }

      Log.v(LOG_TAG, sb.toString());
    }

    boolean injected = injectEvent(event);
    event.recycle();
    return injected;
  }

  private int makePointerAction (int action) {
    return action | ((pointerCount - 1) << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
  }

  private void updatePointers (int x, int y) {
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
  }

  @Override
  public boolean gestureEnabled () {
    return ApplicationContext.havePermission(android.Manifest.permission.INJECT_EVENTS);
  }

  @Override
  public boolean gestureBegin (int x, int y, int fingers) {
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
  public boolean gestureMove (int x, int y) {
    updatePointers(x, y);
    return injectEvent(MotionEvent.ACTION_MOVE);
  }

  @Override
  public boolean gestureEnd () {
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
  public boolean gestureEnd (int x, int y) {
    updatePointers(x, y);
    return gestureEnd();
  }

  public MotionDevice () {
    clearFields();
  }
}
