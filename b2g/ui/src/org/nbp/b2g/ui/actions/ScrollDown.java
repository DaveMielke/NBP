package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollDown extends DirectionalAction {
  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollForwardAction();
  }

  public ScrollDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
