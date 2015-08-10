package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollUp extends DirectionalAction {
  @Override
  protected boolean performInternalAction (Endpoint endpoint) {
    if (endpoint.getLineStart() > 0) {
      endpoint.setLine(0);
    } else if (endpoint.getLineIndent() == 0) {
      return false;
    }

    endpoint.setLineIndent(0);
    return endpoint.write();
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollBackwardAction();
  }

  public ScrollUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
