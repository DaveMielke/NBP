package org.nbp.compass;

public abstract class ApplicationSettings {
  private ApplicationSettings () {
  }

  public volatile static DistanceUnit DISTANCE_UNIT = ApplicationDefaults.DISTANCE_UNIT;
  public volatile static SpeedUnit SPEED_UNIT = ApplicationDefaults.SPEED_UNIT;

  public volatile static boolean LOG_ADDRESSES = ApplicationDefaults.LOG_ADDRESSES;
  public volatile static boolean LOG_VECTORS = ApplicationDefaults.LOG_VECTORS;
}
