package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveLast extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.FORWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return true;
  }

  public MoveLast (Endpoint endpoint) {
    super(endpoint, false);
  }
}
