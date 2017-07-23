package org.nbp.compass;

public interface Unit {
  public String getAcronym ();
  public float getConversion ();

  public final float SECONDS_PER_MINUTE = 60f;
  public final float MINUTES_PER_HOUR = 60f;
  public final float SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

  public final float CENTIMETERS_PER_METER = 1E2f;
  public final float CENTIMETERS_PER_INCH = 2.54f;
  public final float INCHES_PER_FOOT = 12f;
  public final float CENTIMETERS_PER_FOOT = CENTIMETERS_PER_INCH * INCHES_PER_FOOT;
  public final float FEET_PER_METER = CENTIMETERS_PER_METER / CENTIMETERS_PER_FOOT;

  public final float KILOMETERS_PER_METER = 1E-3f;
  public final float FEET_PER_MILE = 5280F;
  public final float MILES_PER_METER = FEET_PER_METER / FEET_PER_MILE;
  public final float METERS_PER_KNOT = 1852f;
}
