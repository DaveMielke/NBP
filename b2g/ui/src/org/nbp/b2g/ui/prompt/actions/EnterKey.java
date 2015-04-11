package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class EnterKey extends PromptAction {
  @Override
  public boolean performAction () {
    boolean done = false;
    PromptEndpoint promptEndpoint = getPromptEndpoint();
    String response = promptEndpoint.getResponse();

    if (response.length() > 0) {
      if (promptEndpoint.done(response)) done = true;
    }

    Endpoints.setHostEndpoint();
    return done;
  }

  public EnterKey (Endpoint endpoint) {
    super(endpoint);
  }
}
