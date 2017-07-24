package org.nbp.compass;

import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static float LOCATION_MINIMUM_DISTANCE = 7f; // meters
  public final static int LOCATION_MINIMUM_TIME = 4000; // milliseconds
  public final static int LOCATION_MAXIMUM_TIME = 10000; // milliseconds
  public final static int LOCATION_FUSED_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

  public final static int ORIENTATION_UPDATE_DELAY = 2000; // milliseconds

  public final static int SENSOR_UPDATE_INTERVAL = 250000 /* microseconds */;
}
