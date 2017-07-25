package org.nbp.compass;

import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int UPDATE_MINIMUM_TIME = 250; // milliseconds
  public final static int ANNOUNCE_MINIMUM_TIME = 2000; // milliseconds

  public final static float LOCATION_UPDATE_RADIUS = 6f; // meters
  public final static int LOCATION_UPDATE_TIME = 4000; // milliseconds
  public final static int LOCATION_FUSED_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

  public final static int SENSOR_UPDATE_INTERVAL = UPDATE_MINIMUM_TIME * 1000; // microseconds;
}
