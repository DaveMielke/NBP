package org.nbp.compass;

import android.util.Log;
import android.os.Bundle;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;

public class FusedLocationMonitor extends LocationMonitor implements
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
  LocationListener
{
  private final static String LOG_TAG = FusedLocationMonitor.class.getName();

  private enum ProviderState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    STARTED
  }

  private ProviderState state = ProviderState.DISCONNECTED;
  private final GoogleApiClient client;

  public FusedLocationMonitor () {
    super();

    client = new GoogleApiClient.Builder(getCompassActivity())
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
  }

  private final void startMonitoring () {
    LocationRequest request = new LocationRequest()
      .setInterval(ApplicationParameters.LOCATION_MAXIMUM_TIME)
      .setFastestInterval(ApplicationParameters.LOCATION_MINIMUM_TIME)
      .setPriority(ApplicationParameters.LOCATION_FUSED_PRIORITY)
      ;

    LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
  }

  private final void stopMonitoring () {
    LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
  }

  @Override
  protected final boolean startProvider () {
    switch (state) {
      case DISCONNECTED:
        client.connect();
        state = ProviderState.CONNECTING;
      case CONNECTING:
        break;

      case CONNECTED:
        startMonitoring();
      case STARTED:
        break;
    }

    return true;
  }

  @Override
  protected final void stopProvider () {
    switch (state) {
      case STARTED:
        stopMonitoring();
        state = ProviderState.CONNECTED;
      case CONNECTED:
        break;
    }
  }

  @Override
  public void onLocationChanged (Location location) {
    setLocation(location);
  }

  @Override
  public void onConnected (Bundle connectionHint) {
    state = ProviderState.CONNECTED;

    startMonitoring();
    state = ProviderState.STARTED;

    setLocation(LocationServices.FusedLocationApi.getLastLocation(client));
  }

  @Override
  public void onConnectionSuspended (int cause) {
    Log.d(LOG_TAG, ("connection suspended: " + cause));
  }

  @Override
  public void onConnectionFailed (ConnectionResult result) {
    state = ProviderState.DISCONNECTED;
    Log.w(LOG_TAG, ("connection failed: " + result.getErrorCode()));
  }
}
