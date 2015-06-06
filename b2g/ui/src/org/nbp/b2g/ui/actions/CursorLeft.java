package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class CursorLeft extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "LEFT";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_LEFT;
  }

  public CursorLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
