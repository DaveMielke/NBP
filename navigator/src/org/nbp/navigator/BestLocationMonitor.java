package org.nbp.navigator;

import android.location.LocationManager;

public class BestLocationMonitor extends ProviderLocationMonitor {
  public BestLocationMonitor () {
    super();
  }

  private final static String[] locationProviders = {
    LocationManager.GPS_PROVIDER,
    LocationManager.NETWORK_PROVIDER
  };

  @Override
  protected final String getLocationProvider () {
    LocationManager lm = getLocationManager();

    for (String provider : locationProviders) {
      if (lm.isProviderEnabled(provider)) return provider;
    }

    return LocationManager.PASSIVE_PROVIDER;
  }
}
