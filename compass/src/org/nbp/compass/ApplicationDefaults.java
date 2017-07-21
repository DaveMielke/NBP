package org.nbp.compass;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static DistanceUnit DISTANCE_UNIT = DistanceUnit.METERS;
  public final static SpeedUnit SPEED_UNIT = SpeedUnit.KILOMETERS_PER_HOUR;

  public final static boolean LOG_ADDRESSES = false;
  public final static boolean LOG_VECTORS = false;
}
