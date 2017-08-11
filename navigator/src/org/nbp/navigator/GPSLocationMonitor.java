package org.nbp.navigator;

import android.location.LocationManager;

public class GPSLocationMonitor extends ProviderLocationMonitor {
  public GPSLocationMonitor () {
    super();
  }

  @Override
  protected final String getLocationProvider () {
    return LocationManager.GPS_PROVIDER;
  }
}
