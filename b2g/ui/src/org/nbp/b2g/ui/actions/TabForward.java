package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class TabForward extends ScanCodeAction {
  @Override
  protected String getScanCode () {
    return "TAB";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_TAB;
  }

  public TabForward () {
    super(false);
  }
}
