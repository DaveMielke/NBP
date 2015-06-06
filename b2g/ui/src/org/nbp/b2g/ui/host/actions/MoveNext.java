package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveNext extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.FORWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return false;
  }

  public MoveNext (Endpoint endpoint) {
    super(endpoint, false);
  }
}
