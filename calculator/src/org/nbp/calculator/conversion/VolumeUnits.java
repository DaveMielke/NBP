package org.nbp.calculator.conversion;

public class VolumeUnits extends UnitType {
  private final static double cube (double value) {
    return value * value * value;
  }

  public VolumeUnits () {
    super("volume", true, "m3", "meters^3", "meter^3");
  }

  public final InternationalUnit CUBIC_METER = (InternationalUnit)getBaseUnit();
  public final Unit CUBIC_CENTIMETER = new Unit(CUBIC_METER.CENTI, 1.0, "cc", "cubiccentimeters", "cubiccentimeter");
  public final InternationalUnit LITER = new InternationalUnit(CUBIC_CENTIMETER, 1000.0, "l", "liters", "liter");

  public final static double CUBIC_METERS_PER_CUBIC_INCH = cube(LengthUnits.METERS_PER_INCH);
  public final Unit CUBIC_INCH = new Unit(CUBIC_METER, CUBIC_METERS_PER_CUBIC_INCH, "cbin", "cubicinches", "cubicinch");

  public final static double CUBIC_INCHES_PER_CUBIC_FOOT = cube(LengthUnits.INCHES_PER_FOOT);
  public final Unit CUBIC_FOOT = new Unit(CUBIC_INCH, CUBIC_INCHES_PER_CUBIC_FOOT, "cbft", "cubicfeet", "cubicfoot");

  public final static double CUBIC_FEET_PER_CUBIC_YARD = cube(LengthUnits.FEET_PER_YARD);
  public final Unit CUBIC_YARD = new Unit(CUBIC_FOOT, CUBIC_FEET_PER_CUBIC_YARD, "cbyd", "cubicyards", "cubicyard");

  public final static double CUBIC_YARDS_PER_CUBIC_ROD = cube(LengthUnits.YARDS_PER_ROD);
  public final Unit CUBIC_ROD = new Unit(CUBIC_YARD, CUBIC_YARDS_PER_CUBIC_ROD, "cbrd", "cubicrods", "cubicrod");

  public final static double CUBIC_RODS_PER_CUBIC_FURLONG = cube(LengthUnits.RODS_PER_FURLONG);
  public final Unit CUBIC_FURLONG = new Unit(CUBIC_ROD, CUBIC_RODS_PER_CUBIC_FURLONG, "cbfur", "cubicfurlongs", "cubicfurlong");

  public final static double CUBIC_FURLONGS_PER_CUBIC_MILE = cube(LengthUnits.FURLONGS_PER_MILE);
  public final Unit CUBIC_MILE = new Unit(CUBIC_FURLONG, CUBIC_FURLONGS_PER_CUBIC_MILE, "cbmi", "cubicmiles", "cubicmile");

  public final static double CUBIC_MILES_PER_CUBIC_LEAGUE = cube(LengthUnits.MILES_PER_LEAGUE);
  public final Unit CUBIC_LEAGUE = new Unit(CUBIC_MILE, CUBIC_MILES_PER_CUBIC_LEAGUE, "cblg", "cubicleagues", "cubicleague");
}