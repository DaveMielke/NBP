package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class Home extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "HOME";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MOVE_HOME;
  }

  public Home (Endpoint endpoint) {
    super(endpoint, false);
  }
}
