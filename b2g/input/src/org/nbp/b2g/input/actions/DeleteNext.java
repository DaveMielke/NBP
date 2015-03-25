package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

import android.view.KeyEvent;

public class DeleteNext extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "DELETE";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_FORWARD_DEL;
  }

  public DeleteNext () {
    super();
  }
}
