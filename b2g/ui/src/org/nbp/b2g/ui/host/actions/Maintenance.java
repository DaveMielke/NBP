package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Maintenance extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return MaintenanceActivity.class;
  }

  public Maintenance (Endpoint endpoint) {
    super(endpoint, true);
  }
}
