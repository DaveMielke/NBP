package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.IntegerControl;

public class LocationIntervalControl extends IntegerControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LocationInterval;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  public String getValue () {
    return ApplicationUtilities.toTimeText(getIntegerValue()).toString();
  }

  @Override
  protected String getPreferenceKey () {
    return "location-interval";
  }

  @Override
  protected final int getIntegerScale () {
    return Unit.MILLISECONDS_PER_SECOND;
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.LOCATION_INTERVAL;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.LOCATION_INTERVAL;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    if (value < 1) return false;
    if (value > ApplicationParameters.LOCATION_MAXIMUM_INTERVAL) return false;

    ApplicationSettings.LOCATION_INTERVAL = value;
    LocationMonitor.restartCurrentMonitor();
    return true;
  }

  public LocationIntervalControl () {
    super();
  }
}
