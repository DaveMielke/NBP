package org.nbp.compass;

import android.location.LocationManager;

public class PassiveLocationMonitor extends ProviderLocationMonitor {
  public PassiveLocationMonitor (CompassActivity activity) {
    super(activity);
  }

  @Override
  protected final String getLocationProvider () {
    return LocationManager.PASSIVE_PROVIDER;
  }
}
