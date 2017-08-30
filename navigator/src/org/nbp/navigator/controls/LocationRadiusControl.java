package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.controls.IntegerControl;
import org.nbp.common.controls.Control;

public class LocationRadiusControl extends IntegerControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LocationRadius;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_location;
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

    Controls.distanceUnit.addOnValueChangedListener(
      new OnValueChangedListener() {
        @Override
        public void onValueChanged (Control control) {
          callOnValueChangedListeners();
        }
      }
    );
  }
}
