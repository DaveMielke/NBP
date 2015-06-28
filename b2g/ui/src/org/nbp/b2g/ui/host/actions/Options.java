package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Options extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return SettingsActivity.class;
  }

  public Options (Endpoint endpoint) {
    super(endpoint, false);
  }
}
