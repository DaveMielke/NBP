package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public class Enter extends ScanCodeAction {
  @Override
  public boolean performAction () {
    return getEndpoint().handleEnterKey();
  }

  @Override
  protected String getScanCode () {
    return "ENTER";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_ENTER;
  }

  public Enter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
