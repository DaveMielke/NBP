package org.nbp.b2g.input;

import android.util.Log;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class KeyCodeAction extends KeyAction {
  private static final String LOG_TAG = KeyCodeAction.class.getName();

  protected final static int NULL_KEY_CODE = KeyEvent.KEYCODE_UNKNOWN;

  private void logKeyCodeEvent (int keyCode, boolean press) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      logKeyEvent("key", KeyEvent.keyCodeToString(keyCode), keyCode, press);
    }
  }

  protected int getKeyCode () {
    return NULL_KEY_CODE;
  }

  @Override
  public boolean performAction () {
    int keyCode = getKeyCode();

    if (keyCode != NULL_KEY_CODE) {
      InputService inputService = getInputService();

      if (inputService != null) {
        InputConnection connection = inputService.getCurrentInputConnection();

        if (connection != null) {
          KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
          logKeyCodeEvent(keyCode, true);

          if (connection.sendKeyEvent(event)) {
            waitForHoldTime();

            event = KeyEvent.changeAction(event, KeyEvent.ACTION_UP);
            logKeyCodeEvent(keyCode, false);

            if (connection.sendKeyEvent(event)) {
              return true;
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
