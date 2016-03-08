package org.nbp.compass;

import android.location.LocationManager;

public class GPSLocationMonitor extends ProviderLocationMonitor {
  @Override
  protected final String getLocationProvider () {
    return LocationManager.GPS_PROVIDER;
  }

  public GPSLocationMonitor (CompassActivity activity) {
    super(activity);
  }
}
