package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class CursorRight extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "RIGHT";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_DPAD_RIGHT;
  }

  public CursorRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
