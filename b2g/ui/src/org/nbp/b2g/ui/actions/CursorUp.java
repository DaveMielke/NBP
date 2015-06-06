package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class CursorUp extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "UP";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_UP;
  }

  public CursorUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
