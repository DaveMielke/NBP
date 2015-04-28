package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveFirst extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "HOME";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MOVE_HOME;
  }

  public MoveFirst (Endpoint endpoint) {
    super(endpoint, false);
  }
}
