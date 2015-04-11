package org.nbp.b2g.ui.prompt;
import org.nbp.b2g.ui.*;

public abstract class PromptAction extends Action {
  protected PromptEndpoint getPromptEndpoint () {
    return (PromptEndpoint)getEndpoint();
  }

  protected PromptAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
