package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollLeft extends DirectionalAction {
  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollFirstAction();
  }

  public ScrollLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
