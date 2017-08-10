package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.TimeControl;

public class UpdateIntervalControl extends TimeControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_UpdateInterval;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  @Override
  protected String getPreferenceKey () {
    return "update-interval";
  }

  @Override
  protected final int getIntegerScale () {
    return MILLISECONDS_PER_SECOND;
  }

  private final static Integer MAXIMUM_VALUE = ApplicationParameters.UPDATE_MAXIMUM_INTERVAL;

  @Override
  protected Integer getIntegerMaximum () {
    return MAXIMUM_VALUE;
  }

  @Override
  protected int getIntegerDefault () {
    return ApplicationDefaults.UPDATE_INTERVAL;
  }

  @Override
  public int getIntegerValue () {
    return ApplicationSettings.UPDATE_INTERVAL;
  }

  @Override
  protected boolean setIntegerValue (int value) {
    ApplicationSettings.UPDATE_INTERVAL = value;
    LocationMonitor.restartCurrentMonitor();
    return true;
  }

  public UpdateIntervalControl () {
    super();
  }
}
