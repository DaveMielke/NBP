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

  public static InputService getInputService () {
    if (inputService == null) Log.w(LOG_TAG, "input service not runnig");
    return inputService;
  }

  public static InputConnection getInputConnection () {
    InputService service = getInputService();
    if (service == null) return null;

    InputConnection connection = service.getCurrentInputConnection();
    if (connection == null) Log.w(LOG_TAG, "no input connection");

    return connection;
  }

  private static Endpoint getHostEndpoint () {
    return Endpoints.getHostEndpoint();
  }

  private static KeyboardMonitor getKeyboardMonitor () {
    return EventMonitors.getKeyboardMonitor();
  }

  private static boolean isKeyboardMonitorRunning () {
    return getKeyboardMonitor().isAlive();
  }

  private static void resetKeys () {
    if (!isKeyboardMonitorRunning()) KeyEvents.resetKeys();
  }

  @Override
  public void onCreate () {
    super.onCreate();
    Log.d(LOG_TAG, "input service started");
    ApplicationContext.setContext(this);
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

    resetKeys();
  }

  @Override
  public void onStartInput (EditorInfo info, boolean restarting) {
    Log.d(LOG_TAG, "input service " + (restarting? "reconnected": "connected"));
    getHostEndpoint().onSelectionChange(info.initialSelStart, info.initialSelEnd);
  }

  @Override
  public void onFinishInput () {
    Log.d(LOG_TAG, "input service disconnected");
    getHostEndpoint().clearSelection();
  }

  @Override
  public void onUpdateSelection (
    int oldSelectionStart, int oldSelectionEnd,
    int newSelectionStart, int newSelectionEnd,
    int candidateStart, int candidateEnd
  ) {
    getHostEndpoint().onSelectionChange(newSelectionStart, newSelectionEnd);

    super.onUpdateSelection(
      oldSelectionStart, oldSelectionEnd,
      newSelectionStart, newSelectionEnd,
      candidateStart, candidateEnd
    );
  }

  public boolean insertText (String string) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.commitText(string, 1)) {
        return true;
      }
    }

    return false;
  }

  public boolean insertText (char character) {
    if (ApplicationParameters.CURRENT_LOG_ACTIONS) {
      Log.d(LOG_TAG, String.format("inserting character: 0X%02X", (int)character));
    }

    if (insertText(Character.toString(character))) return true;
    Log.w(LOG_TAG, String.format("character insertion failed: 0X%02X", (int)character));
    return false;
  }

  private static void logKeyEvent (int code, boolean press) {
    if (ApplicationParameters.CURRENT_LOG_KEYS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key code ");
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
        return isKeyboardMonitorRunning();
    }
  }

  private boolean handleKeyEvent (int code, boolean press) {
    logKeyEvent(code, press);
    if (ignoreKey(code)) return false;

    if ((code >= KeyCode.CURSOR_0) && (code <= KeyCode.CURSOR_19)) {
      KeyEvents.handleCursorKeyEvent((code - KeyCode.CURSOR_0), press);
    } else {
      KeyEvents.handleNavigationKeyEvent(KeyCode.toKeyMask(code), press);
    }

    return true;
  }

  @Override
  public boolean onKeyDown (int code, KeyEvent event) {
    return handleKeyEvent(code, true);
  }

  @Override
  public boolean onKeyUp (int code, KeyEvent event) {
    return handleKeyEvent(code, false);
  }
}
