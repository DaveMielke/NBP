package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class AlertMonitor extends ExternalActivityAction {
  @Override
  protected String getPackageName () {
    return "org.nbp.ipaws";
  }

  public AlertMonitor (Endpoint endpoint) {
    super(endpoint, false);
  }
}
