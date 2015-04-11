package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

public class DeletePrevious extends FindAction {
  @Override
  public boolean performAction (boolean isLongPress) {
    FindEndpoint endpoint = getFindEndpoint();
    return endpoint.deletePrevious(isLongPress);
  }

  public DeletePrevious (Endpoint endpoint) {
    super(endpoint);
  }
}
