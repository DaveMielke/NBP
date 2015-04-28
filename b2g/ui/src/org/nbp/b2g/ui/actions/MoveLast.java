package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveLast extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "END";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MOVE_END;
  }

  public MoveLast (Endpoint endpoint) {
    super(endpoint, false);
  }
}
