package org.nbp.compass;

import android.util.Log;
import android.os.Bundle;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

public class GoogleLocationMonitor extends LocationMonitor implements
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
  LocationListener
{
  private final static String LOG_TAG = GoogleLocationMonitor.class.getName();

  private final GoogleApiClient client;
  private boolean isConnected = false;
  private boolean isStarted = false;

  public GoogleLocationMonitor (CompassActivity activity) {
    super(activity);

    client = new GoogleApiClient.Builder(activity)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();

    Log.d(LOG_TAG, "connecting");
    client.connect();
  }

  private final void startMonitoring () {
    LocationRequest request = new LocationRequest()
      .setInterval(ApplicationParameters.LOCATION_MAXIMUM_INTERVAL)
      .setFastestInterval(ApplicationParameters.LOCATION_MINIMUM_INTERVAL)
      .setPriority(ApplicationParameters.LOCATION_PRIORITY)
      ;

    LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
  }

  private final void stopMonitoring () {
    LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
  }

  @Override
  public final void start () {
    isStarted = true;
    if (isConnected) startMonitoring();
  }

  @Override
  public final void stop () {
    isStarted = false;
    if (isConnected) stopMonitoring();
  }

  @Override
  public void onLocationChanged (Location location) {
    setLocation(location);
  }

  @Override
  public void onConnected (Bundle connectionHint) {
    isConnected = true;
    Log.d(LOG_TAG, "connected");

    setLocation(LocationServices.FusedLocationApi.getLastLocation(client));
    if (isStarted) startMonitoring();
  }

  @Override
  public void onConnectionSuspended (int cause) {
    Log.d(LOG_TAG, ("connection suspended: " + cause));
  }

  @Override
  public void onConnectionFailed (ConnectionResult result) {
    Log.w(LOG_TAG, ("connection failed: " + result.getErrorCode()));
  }
}
