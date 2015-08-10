package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollRight extends DirectionalAction {
  @Override
  protected boolean performInternalAction (Endpoint endpoint) {
    int indent = endpoint.getLineLength() - getBrailleLength();
    if (indent <= endpoint.getLineIndent()) return false;

    endpoint.setLineIndent(indent);
    return endpoint.write();
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollLastAction();
  }

  public ScrollRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
