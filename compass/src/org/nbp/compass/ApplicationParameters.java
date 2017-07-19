package org.nbp.compass;

import android.hardware.SensorManager;
import com.google.android.gms.location.LocationRequest;

public abstract class ApplicationParameters {
  private ApplicationParameters () {
  }

  public final static int MEASUREMENT_DAMPING_FACTOR = 50;

  public final static int SENSOR_UPDATE_INTERVAL = 250000 /* microseconds */;

  public final static int LOCATION_MINIMUM_INTERVAL = 2000;
  public final static int LOCATION_MAXIMUM_INTERVAL = 10000;
  public final static int LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
}
