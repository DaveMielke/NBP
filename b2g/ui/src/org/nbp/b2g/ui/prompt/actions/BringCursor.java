package org.nbp.b2g.ui.prompt.actions;
import org.nbp.b2g.ui.prompt.*;
import org.nbp.b2g.ui.*;

public class BringCursor extends PromptAction {
  @Override
  public boolean performAction (int cursorKey) {
    PromptEndpoint endpoint = getPromptEndpoint();
    return endpoint.bringCursor(cursorKey);
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint);
  }
}
