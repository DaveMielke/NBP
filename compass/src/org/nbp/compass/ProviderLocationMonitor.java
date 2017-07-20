package org.nbp.compass;

import android.util.Log;
import android.os.Bundle;
import android.location.Location;

import android.location.LocationManager;
import android.location.LocationListener;

public abstract class ProviderLocationMonitor extends LocationMonitor implements LocationListener {
  private final static String LOG_TAG = ProviderLocationMonitor.class.getName();

  private LocationManager locationManager;

  public ProviderLocationMonitor (CompassActivity activity) {
    super(activity);
    locationManager = (LocationManager)activity.getSystemService(activity.LOCATION_SERVICE);
    setLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
  }

  protected final LocationManager getLocationManager () {
    return locationManager;
  }

  protected abstract String getLocationProvider ();
  private boolean amMonitoring = false;

  @Override
  public final void start () {
    if (!amMonitoring) {
      String provider = getLocationProvider();
      Log.d(LOG_TAG, ("location provider: " + provider));

      try {
        locationManager.requestLocationUpdates(
          provider,
          ApplicationParameters.LOCATION_MINIMUM_TIME,
          ApplicationParameters.LOCATION_MINIMUM_DISTANCE,
          this
        );

        amMonitoring = true;
      } catch (IllegalArgumentException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
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
