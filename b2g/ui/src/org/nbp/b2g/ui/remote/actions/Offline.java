package org.nbp.b2g.ui.remote.actions;
import org.nbp.b2g.ui.remote.*;
import org.nbp.b2g.ui.*;

public class Offline extends Action {
  @Override
  public boolean performAction () {
    Endpoints.setHostEndpoint();
    return true;
  }

  public Offline (Endpoint endpoint) {
    super(endpoint, false);
  }
}
