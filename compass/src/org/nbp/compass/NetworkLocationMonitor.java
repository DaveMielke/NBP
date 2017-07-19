package org.nbp.compass;

import android.location.LocationManager;

public class NetworkLocationMonitor extends ProviderLocationMonitor {
  public NetworkLocationMonitor (CompassActivity activity) {
    super(activity);
  }

  @Override
  protected final String getLocationProvider () {
    return LocationManager.NETWORK_PROVIDER;
  }
}
