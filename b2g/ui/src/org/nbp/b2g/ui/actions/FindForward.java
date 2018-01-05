package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class FindForward extends Action {
  @Override
  public boolean performAction () {
    return Endpoints.setFindEndpoint();
  }

  public FindForward (Endpoint endpoint) {
    super(endpoint, false);
  }
}
