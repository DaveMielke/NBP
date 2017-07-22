package org.nbp.compass;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static DistanceUnit DISTANCE_UNIT = ApplicationDefaults.DISTANCE_UNIT;
  public volatile static SpeedUnit SPEED_UNIT = ApplicationDefaults.SPEED_UNIT;

  public volatile static boolean LOG_GEOCODING = ApplicationDefaults.LOG_GEOCODING;
  public volatile static boolean LOG_SENSORS = ApplicationDefaults.LOG_SENSORS;
}
