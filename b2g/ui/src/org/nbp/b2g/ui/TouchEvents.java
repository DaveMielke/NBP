package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.hardware.input.InputManager;

import android.view.InputDevice;
import android.view.InputEvent;
import android.view.MotionEvent;

import android.os.SystemClock;

public abstract class TouchEvents {
  private final static String LOG_TAG = TouchEvents.class.getName();

  private static InputManager getInputManager () {
    Object service = ApplicationContext.getSystemService(Context.INPUT_SERVICE);
    if (service != null) return (InputManager)service;

    Log.w(LOG_TAG, "no input manager");
    return null;
  }

  private static boolean injectEvent (InputEvent event) {
    InputManager input = getInputManager();
    if (input == null) return false;

    Integer mode = (Integer)LanguageUtilities.getInstanceField(input, "INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH");
    if (mode == null) return false;

    Boolean injected = (Boolean)LanguageUtilities.invokeInstanceMethod(
      input, "injectInputEvent",
      new Class[] {InputEvent.class, int.class},
      event, mode
    );

    if (injected == null) return false;
    return injected;
  }

  private static boolean injectEvent (int action, int x, int y) {
    long now = SystemClock.uptimeMillis();
    MotionEvent event = MotionEvent.obtain(now, now, action, x, y, 0);
    event.setSource(InputDevice.SOURCE_TOUCHSCREEN);

    boolean injected = injectEvent(event);
    event.recycle();
    return injected;
  //InputManager.getInstance().injectInputEvent(event, InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
  //getInputManager().injectInputEvent(event, true);
  }

  public static boolean tapScreen (int x, int y) {
    if (!injectEvent(MotionEvent.ACTION_DOWN, x, y)) return false;
    if (!injectEvent(MotionEvent.ACTION_UP, x, y)) return false;
    return true;
  }

  private TouchEvents () {
  }
}
