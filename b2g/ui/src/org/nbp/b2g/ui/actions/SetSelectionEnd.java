package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetSelectionEnd extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int end) {
    end = endpoint.getTextOffset(endpoint.findLastBrailleOffset(end) + 1) - 1;
    end += endpoint.getLineStart();

    if (endpoint.isInputArea()) {
      int start = endpoint.getSelectionStart();
      if (!Endpoint.isSelected(start) || (start > end)) start = end;
      if (endpoint.setSelection(start, end+1)) return true;
    } else if (endpoint.setCopyEnd(end)) {
      ApplicationUtilities.message(R.string.CopyToClipboard_action_confirmation);
      return true;
    }

    return false;
  }

  public SetSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
