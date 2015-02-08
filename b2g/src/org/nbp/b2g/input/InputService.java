package org.nbp.b2g.input;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;

public class InputService extends InputMethodService {
  private static final String LOG_TAG = InputService.class.getName();

  private static volatile InputService inputService = null;

  public static InputService getInputService () {
    return inputService;
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "input service started");
    inputService = this;
  }

  @Override
  public void onDestroy () {
    super.onDestroy();

    Log.d(LOG_TAG, "input service stopped");
    inputService = null;
  }

  @Override
  public void onBindInput () {
    Log.d(LOG_TAG, "input service bound");
  }

  @Override
  public void onUnbindInput () {
    Log.d(LOG_TAG, "input service unbound");
  }

  @Override
  public void onStartInput (EditorInfo info, boolean restarting) {
    Log.d(LOG_TAG, "input service " + (restarting? "reconnected": "connected"));
  }

  @Override
  public void onFinishInput () {
    Log.d(LOG_TAG, "input service disconnected");
  }

  public static void logKeyEvent (int code, boolean press, String description) {
    if (ApplicationParameters.LOG_KEYBOARD_EVENTS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key ");
      sb.append((press? "press": "release"));
      sb.append(' ');
      sb.append(description);

      sb.append(": ");
      sb.append(code);
      sb.append(" (");
      sb.append(KeyEvent.keyCodeToString(code));
      sb.append(")");

      Log.d(LOG_TAG, sb.toString());
    }
  }

  public static void logKeyEventReceived (int code, boolean press) {
    logKeyEvent(code, press, "received");
  }

  @Override
  public boolean onKeyDown (int code, KeyEvent event) {
    logKeyEventReceived(code, true);

    return super.onKeyDown(code, event);
  }

  @Override
  public boolean onKeyUp (int code, KeyEvent event) {
    logKeyEventReceived(code, false);

    return super.onKeyUp(code, event);
  }
}
