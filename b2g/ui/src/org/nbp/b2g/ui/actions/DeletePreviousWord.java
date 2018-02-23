package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeletePreviousWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    if (endpoint.isInputArea()) {
      if (endpoint.hasCursor()) {
        CharSequence text = endpoint.getText();
        int length = text.length();

        int end = endpoint.getSelectionEnd();
        int start = findPreviousObject(text, end);
        if (start == NOT_FOUND) start = 0;

        while (end < length) {
          if (!Character.isWhitespace(text.charAt(end))) break;
          end += 1;
        }

        if (end > start) {
          if (endpoint.deleteText(start, end)) {
            return ActionResult.DONE;
          }
        }
      }
    }

    return ActionResult.FAILED;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public DeletePreviousWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
