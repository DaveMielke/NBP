package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

public class DeletePrevious extends PromptAction {
  @Override
  public boolean performAction (boolean isLongPress) {
    PromptEndpoint endpoint = getPromptEndpoint();
    return endpoint.deletePrevious(isLongPress);
  }

  public DeletePrevious (Endpoint endpoint) {
    super(endpoint);
  }
}
