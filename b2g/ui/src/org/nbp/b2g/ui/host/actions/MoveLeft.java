package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class MoveLeft extends MoveBackward {
  @Override
  public boolean performAction () {
    HostEndpoint endpoint = getHostEndpoint();

    synchronized (endpoint) {
      if (endpoint.panLeft()) return true;
      if (endpoint.isEditable()) return false;
    }

    return super.performAction();
  }

  public MoveLeft (Endpoint endpoint) {
    super(endpoint);
  }
}
