package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class RemoteDisplay extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setRemoteEndpoint();
    return true;
  }

  public RemoteDisplay (Endpoint endpoint) {
    super(endpoint, false);
  }
}
