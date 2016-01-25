package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollDown extends DirectionalAction {
  private ActionResult scrollText (Endpoint endpoint) {
    CharSequence text = endpoint.getText();
    int length = text.length();

    int start = endpoint.getLineStart();
    boolean wasBlank = false;

    do {
      int end = endpoint.findNextNewline(start);
      if (end == -1) end = length;
      boolean isBlank = true;

      for (int index=start; index<end; index+=1) {
        if (text.charAt(index) != ' ') {
          isBlank = false;
          break;
        }
      }

      if (wasBlank && !isBlank) {
        endpoint.setLine(start);
        endpoint.setLineIndent(0);
        return ActionResult.WRITE;
      }

      wasBlank = isBlank;
      start = end + 1;
    } while (start <= length);

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
    return getEndpoint().getScrollForwardAction();
  }

  public ScrollDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
