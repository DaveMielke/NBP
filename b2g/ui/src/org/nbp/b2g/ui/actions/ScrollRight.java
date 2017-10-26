package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollRight extends DirectionalAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    endpoint.setCursor(endpoint.getLineStart() + endpoint.getLineLength());
    return ActionResult.DONE;
  }

  @Override
  protected ActionResult performInternalAction (Endpoint endpoint) {
    int indent = endpoint.getLineLength() - getBrailleLength();
    if (indent <= endpoint.getLineIndent()) return ActionResult.FAILED;

    endpoint.setLineIndent(indent);
    return ActionResult.WRITE;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollLastAction();
  }

  public ScrollRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
