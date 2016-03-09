package org.nbp.compass;

import android.location.LocationManager;

public class PassiveLocationMonitor extends ProviderLocationMonitor {
  @Override
  protected final String getLocationProvider () {
    return LocationManager.PASSIVE_PROVIDER;
  }

  public PassiveLocationMonitor (CompassActivity activity) {
    super(activity);
  }
}
