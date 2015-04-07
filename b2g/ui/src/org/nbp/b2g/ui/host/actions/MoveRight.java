package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveRight extends MoveForward {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.panRight()) return true;
      if (endpoint.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveRight (Endpoint endpoint) {
    super(endpoint);
  }
}
