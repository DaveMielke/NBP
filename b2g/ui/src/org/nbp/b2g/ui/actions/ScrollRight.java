package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollRight extends DirectionalAction {
  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollLastAction();
  }

  public ScrollRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
