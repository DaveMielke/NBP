package org.nbp.compass;

import android.util.Log;
import android.os.Bundle;
import android.content.Context;

import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;

public abstract class ProviderLocationMonitor extends LocationMonitor implements LocationListener {
  private final static String LOG_TAG = ProviderLocationMonitor.class.getName();

  private final LocationManager locationManager;

  public ProviderLocationMonitor () {
    super();
    locationManager = (LocationManager)getCompassActivity().getSystemService(Context.LOCATION_SERVICE);
  }

  protected final LocationManager getLocationManager () {
    return locationManager;
  }

  protected abstract String getLocationProvider ();
  private boolean isStarted = false;

  @Override
  protected final boolean startProvider () {
    if (!isStarted) {
      String provider = getLocationProvider();
      Log.d(LOG_TAG, ("location provider: " + provider));
      setLocation(locationManager.getLastKnownLocation(provider));

      try {
        locationManager.requestLocationUpdates(
          provider,
          ApplicationParameters.LOCATION_MINIMUM_TIME,
          ApplicationParameters.LOCATION_MINIMUM_DISTANCE,
          this
        );

        isStarted = true;
      } catch (IllegalArgumentException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
    }

    return isStarted;
  }

  @Override
  protected final void stopProvider () {
    if (isStarted) {
      locationManager.removeUpdates(this);
      isStarted = false;
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
