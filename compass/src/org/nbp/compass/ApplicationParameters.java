package org.nbp.compass;

import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int UPDATE_MINIMUM_TIME = 250; // milliseconds
  public final static int ANNOUNCE_MINIMUM_TIME = 2000; // milliseconds

  public final static int LOCATION_MAXIMUM_RADIUS = 20; // meters
  public final static int LOCATION_MAXIMUM_INTERVAL = 20 * Unit.MILLISECONDS_PER_SECOND; // milliseconds
  public final static int LOCATION_FUSED_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

  public final static int SENSOR_UPDATE_INTERVAL = UPDATE_MINIMUM_TIME * 1000; // microseconds;
}
