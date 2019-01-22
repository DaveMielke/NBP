package org.nbp.b2g.ui;
import org.nbp.b2g.ui.host.HostEndpoint;

import org.nbp.common.SettingsUtilities;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;

import android.view.KeyEvent;

public class InputService extends InputMethodService {
  private final static Class CLASS_OBJECT = InputService.class;
  private final static String LOG_TAG = CLASS_OBJECT.getName();

  public final static void start () {
    String id = CLASS_OBJECT.getPackage().getName() + "/." + CLASS_OBJECT.getSimpleName();
    ApplicationContext.getInputMethodManager().setInputMethod(null, id);
  }

  public static InputMethodInfo getInputMethodInfo () {
    return ApplicationContext.getInputMethodInfo(InputService.class);
  }

  private final static Object inputServiceLock = new Object();
  private volatile static InputService inputService = null;

  public static InputService getInputService () {
    synchronized (inputServiceLock) {
      if (inputService == null) {
        InputMethodInfo info = getInputMethodInfo();

        if (info == null) {
          Log.w(LOG_TAG, "input service not enabled");
        } else if (!info.getId().equals(SettingsUtilities.getSelectedInputMethod())) {
          Log.w(LOG_TAG, "input service not selected");
        } else {
          Log.w(LOG_TAG, "input service not running");
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

  private static HostEndpoint getHostEndpoint () {
    return Endpoints.host.get();
  }

  private int selectionStart = -1;
  private int selectionEnd = -1;

  private final void setSelection (int start, int end) {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      synchronized (this) {
        Log.d(LOG_TAG, String.format(
          "selection change: [%d:%d] -> [%d:%d]",
          selectionStart, selectionEnd,
          start, end
        ));

        selectionStart = start;
        selectionEnd = end;
        endpoint.onInputSelectionChange(start, end);
      }
    }
  }

  public final int getSelectionStart () {
    return selectionStart;
  }

  public final int getSelectionEnd () {
    return selectionEnd;
  }

  public final CharSequence getHintText () {
    EditorInfo info = getCurrentInputEditorInfo();
    if (info == null) return null;

    CharSequence text = info.hintText;
    if (text == null) return null;

    if (text.length() == 0) return null;
    return text;
  }

  private static KeyboardMonitor getKeyboardMonitor () {
    return EventMonitors.keyboard;
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
    setSelection(info.initialSelStart, info.initialSelEnd);
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
      setSelection(newSelectionStart, newSelectionEnd);
    } finally {
      super.onUpdateSelection(
        oldSelectionStart, oldSelectionEnd,
        newSelectionStart, newSelectionEnd,
        candidateStart, candidateEnd
      );
    }
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

  public static boolean injectKey (int... keys) {
    final int count = keys.length;
    int index = 0;

    while (index < count) {
      if (!injectKeyEvent(keys[index++], true)) {
        return false;
      }
    }

    while (index > 0) {
      if (!injectKeyEvent(keys[--index], false)) {
        return false;
      }
    }

    return true;
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
        return false;
    }
  }

  private boolean handleKeyEvent (int code, boolean press) {
    logKeyEvent(code, press);
    if (ignoreKey(code)) return false;

    if ((code >= KeyCode.CURSOR_0) && (code <= KeyCode.CURSOR_19)) {
      KeyEvents.handleCursorKeyEvent((code - KeyCode.CURSOR_0), press);
    } else {
      Integer key = KeyCode.toKey(code);

      if (key != null) {
        KeyEvents.handleNavigationKeyEvent(key, press);
      } else if (KeySet.isKeyboardCode(code)) {
        KeyEvents.handleNavigationKeyEvent(code, press);
      }
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
