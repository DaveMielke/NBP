package org.nbp.compass;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static DistanceUnit DISTANCE_UNIT = ApplicationDefaults.DISTANCE_UNIT;
  public volatile static SpeedUnit SPEED_UNIT = ApplicationDefaults.SPEED_UNIT;
  public volatile static AngleUnit ANGLE_UNIT = ApplicationDefaults.ANGLE_UNIT;
  public volatile static RelativeDirection RELATIVE_DIRECTION = ApplicationDefaults.RELATIVE_DIRECTION;

  public volatile static boolean LOG_GEOCODING = ApplicationDefaults.LOG_GEOCODING;
  public volatile static boolean LOG_SENSORS = ApplicationDefaults.LOG_SENSORS;
  public volatile static LocationProvider LOCATION_PROVIDER = ApplicationDefaults.LOCATION_PROVIDER;
}
