package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;

public class InputService extends InputMethodService {
  private final static String LOG_TAG = InputService.class.getName();

  private static volatile InputService inputService = null;

  public final static int NO_SELECTION = -1;
  private int selectionStart = NO_SELECTION;
  private int selectionEnd = NO_SELECTION;

  public static InputService getInputService () {
    if (inputService == null) Log.w(LOG_TAG, "input service not runnig");
    return inputService;
  }

  public int getSelectionStart () {
    return selectionStart;
  }

  public int getSelectionEnd () {
    return selectionEnd;
  }

  @Override
  public void onCreate () {
    super.onCreate();
    Log.d(LOG_TAG, "input service started");
  }

  @Override
  public void onDestroy () {
    super.onDestroy();

    Log.d(LOG_TAG, "input service stopped");
  }

  @Override
  public void onBindInput () {
    Log.d(LOG_TAG, "input service bound");
    inputService = this;
  }

  @Override
  public void onUnbindInput () {
    Log.d(LOG_TAG, "input service unbound");
    inputService = null;

    if (!KeyboardMonitor.isActive()) KeyEvents.resetKeys();
  }

  @Override
  public void onStartInput (EditorInfo info, boolean restarting) {
    Log.d(LOG_TAG, "input service " + (restarting? "reconnected": "connected"));

    selectionStart = info.initialSelStart;
    selectionEnd = info.initialSelEnd;
  }

  @Override
  public void onFinishInput () {
    Log.d(LOG_TAG, "input service disconnected");

    selectionStart = NO_SELECTION;
    selectionEnd = NO_SELECTION;
  }

  @Override
  public void onUpdateSelection (
    int oldSelectionStart, int oldSelectionEnd,
    int newSelectionStart, int newSelectionEnd,
    int candidateStart, int candidateEnd
  ) {
    selectionStart = newSelectionStart;
    selectionEnd = newSelectionEnd;

    super.onUpdateSelection(
      oldSelectionStart, oldSelectionEnd,
      newSelectionStart, newSelectionEnd,
      candidateStart, candidateEnd
    );
  }

  private InputConnection getInputConnection () {
    InputConnection connection = getCurrentInputConnection();

    if (connection != null) {
      return connection;
    } else {
      Log.w(LOG_TAG, "input service not connected");
    }

    return null;
  }

  public boolean insertCharacter (char character) {
    InputConnection connection = getInputConnection();

    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, String.format("inserting character: 0X%02X", (int)character));
    }

    if (connection != null) {
      if (connection.commitText(Character.toString(character), 1)) {
        return true;
      }
    }

    Log.w(LOG_TAG, String.format("character insertion failed: 0X%02X", (int)character));
    return false;
  }

  private static void logKeyEvent (int code, boolean press) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key ");
      sb.append((press? "press": "release"));
      sb.append(" received");

      sb.append(": ");
      sb.append(code);
      sb.append(" (");
      sb.append(KeyEvent.keyCodeToString(code));
      sb.append(")");

      Log.d(LOG_TAG, sb.toString());
    }
  }

  public static boolean ignoreKey (int code) {
    switch (code) {
      case KeyEvent.KEYCODE_HOME:
      case KeyEvent.KEYCODE_BACK:
        return true;

      default:
        return KeyboardMonitor.isActive();
    }
  }

  @Override
  public boolean onKeyDown (int code, KeyEvent event) {
    logKeyEvent(code, true);
    if (ignoreKey(code)) return false;
    KeyEvents.handleKeyPress(KeyCode.toKeyMask(code));
    return true;
  }

  @Override
  public boolean onKeyUp (int code, KeyEvent event) {
    logKeyEvent(code, false);
    if (ignoreKey(code)) return false;
    KeyEvents.handleKeyRelease(KeyCode.toKeyMask(code));
    return true;
  }
}
