package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PanRight extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.panRight()) return true;
      if (endpoint.hasSoftEdges()) return false;
    }

    return super.performAction();
  }

  public PanRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
