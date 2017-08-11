package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.EnumerationControl;

public class DistanceUnitControl extends EnumerationControl<DistanceUnit> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_DistanceUnit;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_units;
  }

  @Override
  protected String getPreferenceKey () {
    return "distance-unit";
  }

  @Override
  protected DistanceUnit getEnumerationDefault () {
    return ApplicationDefaults.DISTANCE_UNIT;
  }

  @Override
  public DistanceUnit getEnumerationValue () {
    return ApplicationSettings.DISTANCE_UNIT;
  }

  @Override
  protected boolean setEnumerationValue (DistanceUnit value) {
    ApplicationSettings.DISTANCE_UNIT = value;
    return true;
  }

  public DistanceUnitControl () {
    super();
  }
}
