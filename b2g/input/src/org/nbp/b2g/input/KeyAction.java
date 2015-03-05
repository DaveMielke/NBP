package org.nbp.b2g.input;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public final class KeyAction extends Action {
  protected final int keyCode;

  @Override
  public String getActionName () {
    return KeyEvent.keyCodeToString(keyCode);
  }

  public void logKeyEventSent (InputService inputService, int code, boolean press) {
    inputService.logKeyEvent(code, press, "sent");
  }

  @Override
  public final boolean performAction () {
    InputService inputService = getInputService();

    if (inputService != null) {
      InputConnection connection = inputService.getCurrentInputConnection();

      if (connection != null) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);

        if (connection.sendKeyEvent(event)) {
          logKeyEventSent(inputService, keyCode, true);
          event = KeyEvent.changeAction(event, KeyEvent.ACTION_UP);

          if (connection.sendKeyEvent(event)) {
            logKeyEventSent(inputService, keyCode, false);
            return true;
          }
        }
      }
    }

    return false;
  }

  public KeyAction (int keyCode) {
    super();
    this.keyCode = keyCode;
  }
}
