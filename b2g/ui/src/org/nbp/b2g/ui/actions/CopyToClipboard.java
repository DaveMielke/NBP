package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class CopyToClipboard extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();
    CharSequence text = endpoint.getSelectedText();
    if (text == null) text = endpoint.getText();

    if (text != null) {
      if (Clipboard.putText(text)) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.CopyToClipboard_action_confirmation;
  }

  public CopyToClipboard (Endpoint endpoint) {
    super(endpoint, false);
  }
}
