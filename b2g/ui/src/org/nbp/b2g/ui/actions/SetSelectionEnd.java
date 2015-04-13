package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetSelectionEnd extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isEditable()) {
        int end = endpoint.getTextOffset(cursorKey);

        if (endpoint.isCharacterOffset(end)) {
          int start = endpoint.getSelectionStart();
          if (!endpoint.isSelected(start) || (start > end)) start = end;
          if (endpoint.setSelection(start, end+1)) return true;
        }
      }
    }

    return false;
  }

  public SetSelectionEnd (Endpoint endpoint) {
    super(endpoint, false);
  }
}
