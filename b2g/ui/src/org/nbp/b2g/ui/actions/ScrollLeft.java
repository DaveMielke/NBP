package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollLeft extends DirectionalAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    endpoint.setCursor(endpoint.getLineStart());
    return ActionResult.DONE;
  }

  @Override
  protected ActionResult performInternalAction (Endpoint endpoint) {
    if (endpoint.getLineIndent() == 0) return ActionResult.FAILED;
    endpoint.setLineIndent(0);
    return ActionResult.WRITE;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollFirstAction();
  }

  public ScrollLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
