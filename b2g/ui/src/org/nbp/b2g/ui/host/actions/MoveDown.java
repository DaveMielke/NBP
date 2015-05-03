package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveDown extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.FORWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return false;
  }

  @Override
  protected String getScanCode () {
    return "PAGEDOWN";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_PAGE_DOWN;
  }

  public MoveDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
