package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class MoveDown extends DirectionalAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    int end = endpoint.getSelectionEnd();

    if (endpoint.isSelected(end)) {
      if (end != endpoint.getSelectionStart()) end -= 1;
      int start = endpoint.findNextNewline(end);

      if (start != -1) {
        start += 1;
        int length = endpoint.findNextNewline(start);
        if (length == -1) length = endpoint.getTextLength();
        length -= start;

        int before = endpoint.findPreviousNewline(end);
        if (before != -1) end -= before + 1;
        if (end > length) end = length;
        end += start;

        if (endpoint.setCursor(end)) {
          return ActionResult.DONE;
        }
      } else {
        ApplicationUtilities.message(R.string.message_bottom_of_input_area);
      }
    }

    return ActionResult.FAILED;
  }

  @Override
  public ActionResult performInternalAction (Endpoint endpoint) {
    int end = endpoint.getLineStart() + endpoint.getLineLength();
    if (end == endpoint.getTextLength()) return ActionResult.FAILED;

    endpoint.setLine(end+1);
    endpoint.setLineIndent(0);

    return ActionResult.WRITE;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getMoveForwardAction();
  }

  public MoveDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
