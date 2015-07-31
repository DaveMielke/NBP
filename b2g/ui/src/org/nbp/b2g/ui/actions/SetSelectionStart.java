package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetSelectionStart extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        int start = endpoint.getTextOffset(cursorKey);

        if (endpoint.isCharacterOffset(start)) {
          int end = endpoint.getSelectionEnd();
          if (!endpoint.isSelected(end) || (end <= start)) end = start + 1;
          if (endpoint.setSelection(start, end)) return true;
        }
      }
    }

    return false;
  }

  public SetSelectionStart (Endpoint endpoint) {
    super(endpoint, false);
  }
}
