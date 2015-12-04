package org.nbp.b2g.ui;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;

import android.view.KeyEvent;

public class InputService extends InputMethodService {
  private final static String LOG_TAG = InputService.class.getName();

  public static InputMethodInfo getInputMethodInfo () {
    return ApplicationContext.getInputMethodInfo(InputService.class);
  }

  private static boolean restartInputService () {
    return false;
  }

  private final static Object inputServiceLock = new Object();
  private static volatile InputService inputService = null;

  public static InputService getInputService () {
    synchronized (inputServiceLock) {
      if (inputService == null) {
        InputMethodInfo info = getInputMethodInfo();

        if (info == null) {
          Log.w(LOG_TAG, "input service not enabled");
        } else if (!info.getId().equals(ApplicationContext.getSelectedInputMethod())) {
          Log.w(LOG_TAG, "input service not selected");
        } else {
          Log.w(LOG_TAG, "input service not running");

          if (restartInputService()) {
            try {
              inputServiceLock.wait(1000);
            } catch (InterruptedException exception) {
            }
          }
        }
      }

      return inputService;
    }
  }

  public static InputConnection getInputConnection () {
    InputService service = getInputService();
    if (service == null) return null;

    InputConnection connection = service.getCurrentInputConnection();
    if (connection == null) Log.w(LOG_TAG, "no input connection");

    return connection;
  }

  private static Endpoint getHostEndpoint () {
    return Endpoints.host.get();
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
    try {
      Log.d(LOG_TAG, "input service stopped");
    } finally {
      super.onDestroy();
    }
  }

  @Override
  public void onBindInput () {
    Log.d(LOG_TAG, "input service bound");

    synchronized (inputServiceLock) {
      inputService = this;
      inputServiceLock.notify();
    }
  }

  @Override
  public void onUnbindInput () {
    Log.d(LOG_TAG, "input service unbound");

    synchronized (inputServiceLock) {
      inputService = null;
    }

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
    try {
      getHostEndpoint().onSelectionChange(newSelectionStart, newSelectionEnd);
    } finally {
      super.onUpdateSelection(
        oldSelectionStart, oldSelectionEnd,
        newSelectionStart, newSelectionEnd,
        candidateStart, candidateEnd
      );
    }
  }

  public boolean insertText (CharSequence text) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.commitText(text, 1)) {
        return true;
      }
    }

    return false;
  }

  public boolean insertText (char character) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format("inserting character: 0X%02X", (int)character));
    }

    if (insertText(Character.toString(character))) return true;
    Log.w(LOG_TAG, String.format("character insertion failed: 0X%02X", (int)character));
    return false;
  }

  private static void appendKeyCode (StringBuilder sb, int code) {
    sb.append(code);
    String name = KeyEvent.keyCodeToString(code);

    if (name != null) {
      sb.append(" (");
      sb.append(name);
      sb.append(')');
    }
  }

  public static boolean injectKeyEvent (int key, boolean press) {
    if (ApplicationSettings.LOG_ACTIONS) {
      StringBuilder sb = new StringBuilder();

      sb.append("injecting key code ");
      sb.append(press? "press": "release");
      sb.append(": ");
      appendKeyCode(sb, key);

      Log.v(LOG_TAG, sb.toString());
    }

    InputConnection connection = getInputConnection();
    if (connection == null) return false;

    int action = press? KeyEvent.ACTION_DOWN: KeyEvent.ACTION_UP;
    return connection.sendKeyEvent(new KeyEvent(action, key));
  }

  public static boolean injectKey (int key) {
    if (injectKeyEvent(key, true)) {
      if (injectKeyEvent(key, false)) {
        return true;
      }
    }

    return false;
  }

  private static void logKeyEvent (int code, boolean press) {
    if (ApplicationSettings.LOG_KEYBOARD) {
      StringBuilder sb = new StringBuilder();

      sb.append("key code ");
      sb.append((press? "press": "release"));
      sb.append(" received: ");
      appendKeyCode(sb, code);

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
