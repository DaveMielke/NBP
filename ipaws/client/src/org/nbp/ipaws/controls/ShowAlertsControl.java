package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.BooleanControl;

public class ShowAlertsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_ShowAlerts;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "show-alerts";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SHOW_ALERTS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SHOW_ALERTS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SHOW_ALERTS = value;
    return true;
  }

  public ShowAlertsControl () {
    super();
  }
}
