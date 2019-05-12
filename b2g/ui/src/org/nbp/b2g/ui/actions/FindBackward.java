package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class FindBackward extends Action {
  @Override
  public boolean performAction () {
    return Endpoints.setFindEndpoint(true);
  }

  public FindBackward (Endpoint endpoint) {
    super(endpoint, false);
  }
}
