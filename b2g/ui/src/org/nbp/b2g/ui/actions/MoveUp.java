package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveUp extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "PAGEUP";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_PAGE_UP;
  }

  public MoveUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
