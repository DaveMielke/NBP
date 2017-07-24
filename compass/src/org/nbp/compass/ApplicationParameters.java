package org.nbp.compass;

import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static float LOCATION_UPDATE_RADIUS = 6f; // meters
  public final static int LOCATION_UPDATE_TIME = 4000; // milliseconds
  public final static int LOCATION_SHORTEST_TIME = 1000; // milliseconds
  public final static int LOCATION_FUSED_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

  public final static int ORIENTATION_UPDATE_DELAY = 2000; // milliseconds

  public final static int SENSOR_UPDATE_INTERVAL = 250000 /* microseconds */;
}
