package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class EnterKey extends PromptAction {
  @Override
  public boolean performAction () {
    boolean success = false;
    PromptEndpoint endpoint = getPromptEndpoint();
    String response = endpoint.getResponse();

    if (response.length() > 0) {
      if (endpoint.handleResponse(response)) {
        success = true;
      }
    }

    Endpoints.setHostEndpoint();
    return success;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint);
  }
}
