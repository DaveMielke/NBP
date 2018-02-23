package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CutToClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.isInputArea()) {
        CharSequence text = endpoint.getSelectedText();

        if (text != null) {
          if (Clipboard.putText(text)) {
            if (endpoint.deleteSelectedText()) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.CutToClipboard_action_confirmation;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public CutToClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
