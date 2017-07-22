package org.nbp.compass;

import android.hardware.SensorManager;
import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static long LOCATION_MINIMUM_TIME = 4000; // milliseconds
  public final static float LOCATION_MINIMUM_DISTANCE = 7f; // meters

  public final static int LOCATION_MINIMUM_INTERVAL = 2000;
  public final static int LOCATION_MAXIMUM_INTERVAL = 10000;
  public final static int LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

  public final static long ORIENTATION_MINIMUM_TIME = 2000; // milliseconds

  public final static int SENSOR_UPDATE_INTERVAL = 250000 /* microseconds */;
}
