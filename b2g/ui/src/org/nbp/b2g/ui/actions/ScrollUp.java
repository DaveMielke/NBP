package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollUp extends DirectionalAction {
  private ActionResult scrollText (Endpoint endpoint, boolean isInputArea) {
    if (endpoint.getLineStart() > 0) {
      endpoint.setLine(0);
    } else if (endpoint.getLineIndent() == 0) {
      return ActionResult.FAILED;
    }

    endpoint.setLineIndent(0);
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
    return getEndpoint().getScrollBackwardAction();
  }

  public ScrollUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
