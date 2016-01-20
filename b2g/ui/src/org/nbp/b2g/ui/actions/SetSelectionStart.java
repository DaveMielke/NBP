package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetSelectionStart extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int start = endpoint.getAdjustedTextOffset(cursorKey);

      if (endpoint.isCharacterOffset(start)) {
        if (endpoint.isInputArea()) {
          int end = endpoint.getSelectionEnd();
          if (!Endpoint.isSelected(end) || (end <= start)) end = start + 1;
          if (endpoint.setSelection(start, end)) return true;
        } else if (endpoint.setCopyStart(start)) {
          ApplicationUtilities.message(R.string.SetCopyStart_action_confirmation);
          return true;
        }
      }
    }

    return false;
  }

  public SetSelectionStart (Endpoint endpoint) {
    super(endpoint, false);
  }
}
