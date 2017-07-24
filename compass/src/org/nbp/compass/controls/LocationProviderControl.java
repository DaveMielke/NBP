package org.nbp.compass.controls;
import org.nbp.compass.*;

import org.nbp.common.EnumerationControl;

public class LocationProviderControl extends EnumerationControl<LocationProvider> {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_LocationProvider;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_developer;
  }

  @Override
  protected String getPreferenceKey () {
    return "location-provider";
  }

  @Override
  protected LocationProvider getEnumerationDefault () {
    return ApplicationDefaults.LOCATION_PROVIDER;
  }

  @Override
  public LocationProvider getEnumerationValue () {
    return ApplicationSettings.LOCATION_PROVIDER;
  }

  @Override
  protected boolean setEnumerationValue (LocationProvider value) {
    LocationProvider oldProvider = ApplicationSettings.LOCATION_PROVIDER;

    if (value != oldProvider) {
      LocationMonitor oldMonitor = oldProvider.getMonitor();
      boolean started = oldMonitor.isStarted();
      if (started) oldMonitor.stopMonitor();
      ApplicationSettings.LOCATION_PROVIDER = value;

      if (started) {
        LocationMonitor newMonitor = value.getMonitor();
        newMonitor.startMonitor();
      }
    }

    return true;
  }

  public LocationProviderControl () {
    super();
  }
}
