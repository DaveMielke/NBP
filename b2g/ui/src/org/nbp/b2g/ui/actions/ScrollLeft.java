package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollLeft extends DirectionalAction {
  @Override
  protected boolean performInternalAction (Endpoint endpoint) {
    if (endpoint.getLineIndent() == 0) return false;
    endpoint.setLineIndent(0);
    return endpoint.write();
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollFirstAction();
  }

  public ScrollLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
