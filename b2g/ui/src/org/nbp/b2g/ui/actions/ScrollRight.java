package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollRight extends DirectionalAction {
  private ActionResult scrollText (Endpoint endpoint, boolean isInputArea) {
    int indent = endpoint.getLineLength() - getBrailleLength();
    if (isInputArea) indent += 1;
    if (indent <= endpoint.getLineIndent()) return ActionResult.FAILED;

    endpoint.setLineIndent(indent);
    return ActionResult.WRITE;
  }

  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return scrollText(endpoint, true);
  }

  @Override
  protected ActionResult performInternalAction (Endpoint endpoint) {
    return scrollText(endpoint, false);
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollLastAction();
  }

  public ScrollRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
