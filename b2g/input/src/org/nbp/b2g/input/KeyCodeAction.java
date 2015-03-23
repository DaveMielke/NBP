package org.nbp.b2g.input;

import android.util.Log;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public abstract class KeyCodeAction extends KeyAction {
  private static final String LOG_TAG = KeyCodeAction.class.getName();

  protected final static int NULL_KEY_CODE = KeyEvent.KEYCODE_UNKNOWN;

  protected static final int KEY_CODE_SHIFT = KeyEvent.KEYCODE_SHIFT_LEFT;
  protected static final int KEY_CODE_CONTROL = KeyEvent.KEYCODE_CTRL_LEFT;
  protected static final int KEY_CODE_ALT = KeyEvent.KEYCODE_ALT_LEFT;
  protected static final int KEY_CODE_ALTGR = KeyEvent.KEYCODE_ALT_RIGHT;
  protected static final int KEY_CODE_GUI = KeyEvent.KEYCODE_WINDOW;

  protected int[] getKeyCodeModifiers () {
    return null;
  }

  protected int getKeyCode () {
    return NULL_KEY_CODE;
  }

  private void log (int keyCode, boolean press) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      logKeyEvent("key", KeyEvent.keyCodeToString(keyCode), keyCode, press);
    }
  }

  private boolean press (InputConnection connection, int keyCode) {
    log(keyCode, true);
    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
    return connection.sendKeyEvent(event);
  }

  private boolean press (InputConnection connection, int[] keyCodes) {
    if (keyCodes != null) {
      for (int keyCode : keyCodes) {
        if (!press(connection, keyCode)) return false;
      }
    }

    return true;
  }

  private boolean release (InputConnection connection, int keyCode) {
    log(keyCode, false);
    KeyEvent event = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
    return connection.sendKeyEvent(event);
  }

  private boolean release (InputConnection connection, int[] keyCodes) {
    if (keyCodes != null) {
      for (int keyCode : keyCodes) {
        if (!release(connection, keyCode)) return false;
      }
    }

    return true;
  }

  @Override
  public boolean performAction () {
    int keyCode = getKeyCode();

    if (keyCode != NULL_KEY_CODE) {
      InputService inputService = getInputService();

      if (inputService != null) {
        InputConnection connection = inputService.getCurrentInputConnection();

        if (connection != null) {
          int[] modifiers = getKeyCodeModifiers();

          if (press(connection, modifiers)) {
            if (press(connection, keyCode)) {
              waitForHoldTime();

              if (release(connection, keyCode)) {
                if (release(connection, modifiers)) {
                  return true;
                }
              }
            }
          }
        }
      }
    }

    return false;
  }

  protected KeyCodeAction () {
    super();
  }
}
