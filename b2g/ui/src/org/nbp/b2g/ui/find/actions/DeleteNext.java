package org.nbp.b2g.ui.find.actions;
import org.nbp.b2g.ui.find.*;
import org.nbp.b2g.ui.*;

public class DeleteNext extends FindAction {
  @Override
  public boolean performAction () {
    FindEndpoint endpoint = getFindEndpoint();
    return endpoint.deleteNext();
  }

  public DeleteNext (Endpoint endpoint) {
    super(endpoint);
  }
}
