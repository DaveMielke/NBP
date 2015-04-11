package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

public class DeleteNext extends PromptAction {
  @Override
  public boolean performAction (boolean isLongPress) {
    PromptEndpoint endpoint = getPromptEndpoint();
    return endpoint.deleteNext(isLongPress);
  }

  public DeleteNext (Endpoint endpoint) {
    super(endpoint);
  }
}
