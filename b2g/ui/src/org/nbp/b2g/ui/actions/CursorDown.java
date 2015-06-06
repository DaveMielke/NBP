package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class CursorDown extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "DOWN";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_DOWN;
  }

  public CursorDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
