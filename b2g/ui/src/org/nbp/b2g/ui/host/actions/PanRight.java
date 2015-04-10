package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class PanRight extends MoveForward {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.panRight()) return true;
      if (endpoint.hasSoftEdges()) return false;
    }

    return super.performAction();
  }

  public PanRight (Endpoint endpoint) {
    super(endpoint);
  }
}
