package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.controls.BooleanControl;

public class RemoteDisplay extends Action {
  @Override
  public boolean performAction () {
    BooleanControl control = Controls.remoteDisplay;

    if (control.getBooleanValue()) {
      Endpoints.setRemoteEndpoint();
    } else {
      control.confirmValue();
    }

    return true;
  }

  public RemoteDisplay (Endpoint endpoint) {
    super(endpoint, false);
  }
}
