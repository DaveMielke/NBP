package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.BooleanControl;

public class LogGeocodingControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogGeocoding;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-geocoding";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_GEOCODING;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_GEOCODING;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_GEOCODING = value;
    return true;
  }

  public LogGeocodingControl () {
    super();
  }
}
