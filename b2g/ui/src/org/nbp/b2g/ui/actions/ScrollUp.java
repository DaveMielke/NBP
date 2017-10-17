package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollUp extends LineAction {
  private ActionResult scrollText (Endpoint endpoint) {
    CharSequence text = endpoint.getText();

    int start = endpoint.getLineStart();
    int greatestIndent = 0;
    boolean wasBlank = true;

    while (start > 0) {
      int end = start - 1;
      start = endpoint.findPreviousNewline(end) + 1;
      boolean isBlank = true;

      for (int index=start; index<end; index+=1) {
        if (text.charAt(index) != ' ') {
          {
            int indent = index - start;
            if (indent < greatestIndent) return setLine(endpoint, end+1);
            greatestIndent = indent;
          }

          isBlank = false;
          break;
        }
      }

      if (isBlank) {
        if (!wasBlank) return setLine(endpoint, end+1);
      } else {
        if (start == 0) return setLine(endpoint, start);
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
