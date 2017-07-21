package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.BooleanControl;

public class LogVectorsControl extends BooleanControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LogVectors;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "log-vectors";
  }

  @Override
  protected boolean getBooleanDefault () {
    return ApplicationDefaults.LOG_VECTORS;
  }

  @Override
  public boolean getBooleanValue () {
    return ApplicationSettings.LOG_VECTORS;
  }

  @Override
  protected boolean setBooleanValue (boolean value) {
    ApplicationSettings.LOG_VECTORS = value;
    return true;
  }

  public LogVectorsControl () {
    super();
  }
}
