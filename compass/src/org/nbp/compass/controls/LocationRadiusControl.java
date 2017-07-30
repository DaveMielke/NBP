package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.IntegerControl;

public class LocationRadiusControl extends IntegerControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LocationRadius;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  public CharSequence getValue () {
    return ApplicationUtilities.toDistanceText(getIntegerValue());
  }

  @Override
  protected String getPreferenceKey () {
    return "location-radius";
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.LOCATION_RADIUS;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.LOCATION_RADIUS;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    if (value < 1) return false;
    if (value > ApplicationParameters.LOCATION_MAXIMUM_RADIUS) return false;

    ApplicationSettings.LOCATION_RADIUS = value;
    LocationMonitor.restartCurrentMonitor();
    return true;
  }

  public LocationRadiusControl () {
    super();
  }
}
