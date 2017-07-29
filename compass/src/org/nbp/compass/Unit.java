package org.nbp.compass;

public interface Unit {
  public String getSymbol ();
  public float getMultiplier ();

  public final static float GRADIANS_PER_FULL_TURN = 400f;
  public final static float MILS_PER_FULL_TURN = (float)(Math.PI * 2E3d);

  public final static float DEGREES_PER_FULL_TURN = 360f;
  public final static float DEGREES_PER_HALF_TURN = DEGREES_PER_FULL_TURN / 2f;
  public final static float DEGREES_PER_QUARTER_TURN = DEGREES_PER_HALF_TURN / 2f;

  public final static int ARCMINUTES_PER_DEGREE = 60;
  public final static int ARCSECONDS_PER_ARCMINUTE = 60;
  public final static int ARCSECONDS_PER_DEGREE = ARCMINUTES_PER_DEGREE * ARCSECONDS_PER_ARCMINUTE;

  public final float SECONDS_PER_MINUTE = 60f;
  public final float MINUTES_PER_HOUR = 60f;
  public final float SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

  public final float METERS_PER_KILOMETER = 1E3f;
  public final float METERS_PER_KNOT = 1852f;
  public final float CENTIMETERS_PER_METER = 1E2f;
  public final float CENTIMETERS_PER_INCH = 2.54f;
  public final float INCHES_PER_FOOT = 12f;
  public final float FEET_PER_MILE = 5280F;

  public final float CENTIMETERS_PER_FOOT = CENTIMETERS_PER_INCH * INCHES_PER_FOOT;
  public final float FEET_PER_METER = CENTIMETERS_PER_METER / CENTIMETERS_PER_FOOT;
  public final float MILES_PER_METER = FEET_PER_METER / FEET_PER_MILE;
}
