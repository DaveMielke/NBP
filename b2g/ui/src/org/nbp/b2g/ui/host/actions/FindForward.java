package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class FindForward extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setFindEndpoint();
    return true;
  }

  public FindForward (Endpoint endpoint) {
    super(endpoint, false);
  }
}
