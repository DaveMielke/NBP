package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveFirst extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.BACKWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return true;
  }

  @Override
  protected String getScanCode () {
    return "HOME";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MOVE_HOME;
  }

  public MoveFirst (Endpoint endpoint) {
    super(endpoint, false);
  }
}
