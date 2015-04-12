package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class PowerButton extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "POWER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_POWER;
  }

  public PowerButton (Endpoint endpoint) {
    super(endpoint, false);
  }
}
