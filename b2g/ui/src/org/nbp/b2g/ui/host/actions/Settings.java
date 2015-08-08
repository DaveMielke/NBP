package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class Settings extends InternalActivityAction {
  @Override
  protected Class getActivityClass () {
    return SettingsActivity.class;
  }

  public Settings (Endpoint endpoint) {
    super(endpoint, false);
  }
}
