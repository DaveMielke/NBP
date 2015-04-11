package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class EnterKey extends PromptAction {
  @Override
  public boolean performAction () {
    PromptEndpoint endpoint = getPromptEndpoint();
    boolean success = endpoint.handleResponse(endpoint.getResponse());
    Endpoints.setHostEndpoint();
    return success;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint);
  }
}
