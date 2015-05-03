package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.KeyEvent;

public class MoveLast extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.FORWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return true;
  }

  @Override
  protected String getScanCode () {
    return "END";
  }

  @Override
  protected int getKeyCode () {
    return KeyEvent.KEYCODE_MOVE_END;
  }

  public MoveLast (Endpoint endpoint) {
    super(endpoint, false);
  }
}
