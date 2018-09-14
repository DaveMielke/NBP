package org.nbp.ipaws.controls;
import org.nbp.ipaws.*;

import org.nbp.common.controls.BooleanControl;

public class SpeakAlertsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeakAlerts;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "speak-alerts";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.SPEAK_ALERTS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.SPEAK_ALERTS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.SPEAK_ALERTS = value;
    return true;
  }

  public SpeakAlertsControl () {
    super();
  }
}
