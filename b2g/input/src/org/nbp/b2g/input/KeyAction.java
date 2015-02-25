package org.nbp.b2g.input;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public final class KeyAction extends Action {
  protected final int keyCode;

  @Override
  public final boolean performAction () {
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

    return false;
  }

  public KeyAction (InputService inputService, int keyCode) {
    super(inputService);
    this.keyCode = keyCode;
  }
}
