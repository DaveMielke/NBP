package org.nbp.b2g.input;

import android.util.Log;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public final class KeyAction extends Action {
  private static final String LOG_TAG = KeyAction.class.getName();

  protected final int keyCode;
  protected final int globalAction;

  @Override
  public String getActionName () {
    return KeyEvent.keyCodeToString(keyCode);
  }

  @Override
  public final boolean performAction () {
    if (globalAction != 0) {
      ScreenMonitor monitor = ScreenMonitor.getScreenMonitor();

      if (monitor != null) {
        return monitor.performGlobalAction(globalAction);
      }
    }

    if (keyCode != KeyEvent.KEYCODE_UNKNOWN) {
      InputService inputService = getInputService();

      if (inputService != null) {
        InputConnection connection = inputService.getCurrentInputConnection();

        if (connection != null) {
          KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);

          if (connection.sendKeyEvent(event)) {
            event = KeyEvent.changeAction(event, KeyEvent.ACTION_UP);

            if (connection.sendKeyEvent(event)) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  public KeyAction (int keyCode, int globalAction) {
    super();
    this.keyCode = keyCode;
    this.globalAction = globalAction;
  }

  public static void add (int keyMask, int keyCode, int globalAction) {
    add(keyMask, new KeyAction(keyCode, globalAction));
  }

  public KeyAction (int keyCode) {
    this(keyCode, 0);
  }

  public static void add (int keyMask, int keyCode) {
    add(keyMask, new KeyAction(keyCode));
  }
}
