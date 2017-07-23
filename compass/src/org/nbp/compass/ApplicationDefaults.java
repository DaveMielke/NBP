package org.nbp.compass;

public abstract class ApplicationDefaults {
  private ApplicationDefaults () {
  }

  public final static AngleUnit ANGLE_UNIT = AngleUnit.DEGREES;
  public final static DistanceUnit DISTANCE_UNIT = DistanceUnit.FEET;
  public final static SpeedUnit SPEED_UNIT = SpeedUnit.MPH;

  public final static boolean LOG_GEOCODING = false;
  public final static boolean LOG_SENSORS = false;
}
