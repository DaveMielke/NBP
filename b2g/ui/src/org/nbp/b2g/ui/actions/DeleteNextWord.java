package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeleteNextWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    if (endpoint.isInputArea()) {
      if (endpoint.hasCursor()) {
        CharSequence text = endpoint.getText();
        int length = text.length();

        int start = endpoint.getSelectionStart();
        int end = start;

        boolean removeSpaceBefore = false;
        boolean removeSpaceAfter = false;

        if (start == length) {
          removeSpaceBefore = true;
        } else if (Character.isWhitespace(text.charAt(start))) {
          removeSpaceBefore = true;

          while (++end < length) {
            if (!Character.isWhitespace(text.charAt(end))) break;
          }
        } else if (end == 0) {
          removeSpaceAfter = true;
        } else if (Character.isWhitespace(text.charAt(end-1))) {
          removeSpaceAfter = true;
        }

        if (removeSpaceBefore) {
          while (start > 0) {
            if (!Character.isWhitespace(text.charAt(--start))) {
              start += 1;
              break;
            }
          }
        }

        while (++end < length) {
          if (Character.isWhitespace(text.charAt(end))) break;
        }

        if (removeSpaceAfter) {
          while (++end < length) {
            if (!Character.isWhitespace(text.charAt(end))) break;
          }
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

  public DeleteNextWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
