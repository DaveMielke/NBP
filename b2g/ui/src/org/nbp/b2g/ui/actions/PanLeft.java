package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PanLeft extends Action {
  @Override
  public boolean performAction () {
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      if (endpoint.panLeft()) return true;
      if (endpoint.hasSoftEdges()) return false;
    }

    return super.performAction();
  }

  public PanLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
