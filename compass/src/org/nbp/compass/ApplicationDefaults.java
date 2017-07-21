package org.nbp.compass;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static DistanceUnit DISTANCE_UNIT = DistanceUnit.FEET;
  public final static SpeedUnit SPEED_UNIT = SpeedUnit.MPH;

  public final static boolean LOG_ADDRESSES = false;
  public final static boolean LOG_VECTORS = false;
}
