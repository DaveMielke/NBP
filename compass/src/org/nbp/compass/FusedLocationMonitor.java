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

  private enum MonitorState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    STARTED
  }

  private MonitorState state = MonitorState.DISCONNECTED;
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
      .setSmallestDisplacement(ApplicationParameters.LOCATION_UPDATE_RADIUS)
      .setInterval(ApplicationParameters.LOCATION_UPDATE_TIME)
      .setFastestInterval(ApplicationParameters.LOCATION_SHORTEST_TIME)
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
        Log.d(LOG_TAG, "connecting");
        client.connect();
        state = MonitorState.CONNECTING;
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
        state = MonitorState.CONNECTED;
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
    state = MonitorState.CONNECTED;
    Log.d(LOG_TAG, "connected");

    startMonitoring();
    state = MonitorState.STARTED;

    setLocation(LocationServices.FusedLocationApi.getLastLocation(client));
  }

  @Override
  public void onConnectionSuspended (int cause) {
    Log.d(LOG_TAG, ("connection suspended: " + cause));
  }

  @Override
  public void onConnectionFailed (ConnectionResult result) {
    state = MonitorState.DISCONNECTED;
    Log.w(LOG_TAG, ("connection failed: " + result.getErrorCode()));
  }
}
