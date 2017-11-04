package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class ScrollToFirst extends ScrollAction {
  @Override
  protected ScrollDirection getScrollDirection () {
    return ScrollDirection.BACKWARD;
  }

  @Override
  protected boolean getContinueScrolling () {
    return true;
  }

  public ScrollToFirst (Endpoint endpoint) {
    super(endpoint, false);
  }
}
