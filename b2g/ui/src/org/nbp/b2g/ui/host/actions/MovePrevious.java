package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MovePrevious extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.BACKWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return false;
  }

  public MovePrevious (Endpoint endpoint) {
    super(endpoint, false);
  }
}
