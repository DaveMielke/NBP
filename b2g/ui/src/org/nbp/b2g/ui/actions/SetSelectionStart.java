package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetSelectionStart extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int start) {
    start += endpoint.getLineStart();

    if (endpoint.isInputArea()) {
      int end = endpoint.getSelectionEnd();
      if (!Endpoint.isSelected(end) || (end <= start)) end = start + 1;
      if (endpoint.setSelection(start, end)) return true;
    } else if (endpoint.setCopyStart(start)) {
      ApplicationUtilities.message(R.string.SetCopyStart_action_confirmation);
      return true;
    }

    return false;
  }

  public SetSelectionStart (Endpoint endpoint) {
    super(endpoint, true);
  }
}
