package org.nbp.b2g.input;

import android.util.Log;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class KeyAction extends Action {
  private static final String LOG_TAG = KeyAction.class.getName();

  protected final int keyCode;

  @Override
  public boolean performAction () {
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

  protected KeyAction (int keyCode, String name) {
    super(name);
    this.keyCode = keyCode;
  }

  public KeyAction (int keyCode) {
    this(keyCode, KeyEvent.keyCodeToString(keyCode));
  }

  public static void add (int keyMask, int keyCode) {
    add(keyMask, new KeyAction(keyCode));
  }
}
