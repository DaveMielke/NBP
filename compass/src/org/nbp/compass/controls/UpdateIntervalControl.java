package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.IntegerControl;

public class UpdateIntervalControl extends IntegerControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_UpdateInterval;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_general;
  }

  public CharSequence getValue () {
    return ApplicationUtilities.toTimeText(getIntegerValue());
  }

  @Override
  protected String getPreferenceKey () {
    return "update-interval";
  }

  @Override
  protected final int getIntegerScale () {
    return Unit.MILLISECONDS_PER_SECOND;
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
    if (value < 1) return false;
    if (value > ApplicationParameters.UPDATE_MAXIMUM_INTERVAL) return false;

    ApplicationSettings.UPDATE_INTERVAL = value;
    LocationMonitor.restartCurrentMonitor();
    return true;
  }

  public UpdateIntervalControl () {
    super();
  }
}
