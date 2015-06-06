package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class PageDown extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "PAGEDOWN";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_PAGE_DOWN;
  }

  public PageDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
