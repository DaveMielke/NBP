package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

public class DeleteNext extends FindAction {
  @Override
  public boolean performAction (boolean isLongPress) {
    FindEndpoint endpoint = getFindEndpoint();
    return endpoint.deleteNext(isLongPress);
  }

  public DeleteNext (Endpoint endpoint) {
    super(endpoint);
  }
}
