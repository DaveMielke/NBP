package org.nbp.compass;

import android.location.LocationManager;

public class NetworkLocationMonitor extends ProviderLocationMonitor {
  @Override
  protected final String getLocationProvider () {
    return LocationManager.NETWORK_PROVIDER;
  }

  public NetworkLocationMonitor (CompassActivity activity) {
    super(activity);
  }
}
