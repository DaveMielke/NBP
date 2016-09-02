package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class AirplaneModeOff extends AirplaneModeAction {
  @Override
  protected final String getSettingValue () {
    return FALSE;
  }

  @Override
  protected final int getSettingDescription () {
    return OFF;
  }

  public AirplaneModeOff (Endpoint endpoint) {
    super(endpoint);
  }
}
