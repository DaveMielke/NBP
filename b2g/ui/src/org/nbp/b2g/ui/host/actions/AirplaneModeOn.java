package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class AirplaneModeOn extends AirplaneModeAction {
  @Override
  protected final String getSettingValue () {
    return TRUE;
  }

  @Override
  protected final int getSettingDescription () {
    return ON;
  }

  public AirplaneModeOn (Endpoint endpoint) {
    super(endpoint);
  }
}
