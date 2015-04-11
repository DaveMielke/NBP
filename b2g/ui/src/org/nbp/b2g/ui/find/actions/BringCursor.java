package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

public class BringCursor extends FindAction {
  @Override
  public boolean performAction (int cursorKey) {
    FindEndpoint endpoint = getFindEndpoint();
    return endpoint.bringCursor(cursorKey);
  }

  public BringCursor (Endpoint endpoint) {
    super(endpoint);
  }
}
