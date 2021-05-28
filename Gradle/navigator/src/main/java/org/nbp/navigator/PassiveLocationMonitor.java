package org.nbp.navigator;

import android.location.LocationManager;

public class PassiveLocationMonitor extends ProviderLocationMonitor {
  public PassiveLocationMonitor () {
    super();
  }

  @Override
  protected final String getLocationProvider () {
    return LocationManager.PASSIVE_PROVIDER;
  }
}
