package org.nbp.b2g.ui.actions;
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

  public EnterKey () {
    super(false);
  }
}
