package org.nbp.compass;

import android.util.Log;
import android.os.Bundle;
import android.location.Location;

import android.location.LocationManager;
import android.location.LocationListener;

public abstract class ProviderLocationMonitor extends LocationMonitor implements LocationListener {
  private final static String LOG_TAG = ProviderLocationMonitor.class.getName();

  protected abstract String getLocationProvider ();

  private LocationManager locationManager;
  private boolean amMonitoring = false;

  public ProviderLocationMonitor (CompassActivity activity) {
    super(activity);

    locationManager = (LocationManager)activity.getSystemService(activity.LOCATION_SERVICE);
    setLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
  }

  @Override
  public final void start () {
    try {
      locationManager.requestLocationUpdates(
        getLocationProvider(), 10000, 10f, this
      );

      amMonitoring = true;
    } catch (IllegalArgumentException exception) {
      Log.w(LOG_TAG, exception.getMessage());
    }
  }

  @Override
  public final void stop () {
    if (amMonitoring) {
      locationManager.removeUpdates(this);
      amMonitoring = false;
    }
  }

  @Override
  public void onLocationChanged (Location location) {
    setLocation(location);
  }

  @Override
  public void onStatusChanged (String provider, int status, Bundle extras) {
    Log.w(LOG_TAG,
      String.format(
        "provider status changed: %s: %d",
        provider, status
      )
    );
  }

  @Override
  public void onProviderEnabled (String provider) {
    Log.i(LOG_TAG, ("provider enabled: " + provider));
  }

  @Override
  public void onProviderDisabled (String provider) {
    Log.w(LOG_TAG, ("provider disabled: " + provider));
  }
}
