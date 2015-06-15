package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Clock extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return ClockActivity.class;
  }

  public Clock (Endpoint endpoint) {
    super(endpoint, false);
  }
}
