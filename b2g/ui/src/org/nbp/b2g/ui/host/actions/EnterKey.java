package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class EnterKey extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "ENTER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ENTER;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint, false);
  }
}
