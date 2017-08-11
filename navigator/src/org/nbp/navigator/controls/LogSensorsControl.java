package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.BooleanControl;

public class LogSensorsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogSensors;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-sensors";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_SENSORS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_SENSORS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_SENSORS = value;
    return true;
  }

  public LogSensorsControl () {
    super();
  }
}
