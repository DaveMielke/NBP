package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class MoveUp extends DirectionalAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    int start = endpoint.getSelectionStart();

    if (endpoint.isSelected(start)) {
      int after = endpoint.findPreviousNewline(start);

      if (after != -1) {
        int offset = start - after - 1;

        int before = endpoint.findPreviousNewline(after);
        start = (before == -1)? 0: (before + 1);

        int length = after - start;
        if (offset > length) offset = length;
        start += offset;

        if (endpoint.setCursor(start)) {
          return ActionResult.DONE;
        }
      } else {
        ApplicationUtilities.message(R.string.message_top_of_input_area);
      }
    }

    return ActionResult.FAILED;
  }

  @Override
  public ActionResult performInternalAction (Endpoint endpoint) {
    int start = endpoint.getLineStart();
    if (start == 0) return ActionResult.FAILED;

    endpoint.setLine(start-1);
    endpoint.setLineIndent(0);

    return ActionResult.WRITE;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getMoveBackwardAction();
  }

  public MoveUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
