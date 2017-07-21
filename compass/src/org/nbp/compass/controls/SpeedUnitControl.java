package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.EnumerationControl;

public class SpeedUnitControl extends EnumerationControl<SpeedUnit> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeedUnit;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "speed-unit";
  }

  @Override
  protected SpeedUnit getEnumerationDefault () {
    return ApplicationDefaults.SPEED_UNIT;
  }

  @Override
  public SpeedUnit getEnumerationValue () {
    return ApplicationSettings.SPEED_UNIT;
  }

  @Override
  protected boolean setEnumerationValue (SpeedUnit value) {
    ApplicationSettings.SPEED_UNIT = value;
    return true;
  }

  public SpeedUnitControl () {
    super();
  }
}
