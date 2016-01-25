package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollUp extends DirectionalAction {
  private ActionResult scrollText (Endpoint endpoint) {
    CharSequence text = endpoint.getText();
    boolean wasBlank = true;
    int start = endpoint.getLineStart();

    while (start > 0) {
      int end = start - 1;
      start = endpoint.findPreviousNewline(end) + 1;
      boolean isBlank = true;

      for (int index=start; index<end; index+=1) {
        if (text.charAt(index) != ' ') {
          isBlank = false;
          break;
        }
      }

      if (!isBlank && (start == 0)) {
        endpoint.setLine(start);
        endpoint.setLineIndent(0);
        return ActionResult.WRITE;
      }

      if (isBlank && !wasBlank) {
        endpoint.setLine(end+1);
        endpoint.setLineIndent(0);
        return ActionResult.WRITE;
      }

      wasBlank = isBlank;
    }

    return ActionResult.FAILED;
  }

  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return scrollText(endpoint);
  }

  @Override
  protected ActionResult performInternalAction (Endpoint endpoint) {
    return scrollText(endpoint);
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollBackwardAction();
  }

  public ScrollUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
