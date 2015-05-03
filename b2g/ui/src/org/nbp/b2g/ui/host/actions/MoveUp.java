package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveUp extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.BACKWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return false;
  }

  @Override
  protected String getScanCode () {
    return "PAGEUP";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_PAGE_UP;
  }

  public MoveUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
