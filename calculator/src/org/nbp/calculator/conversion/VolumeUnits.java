package org.nbp.calculator.conversion;

public class VolumeUnits extends UnitType {
  private final static double cube (double value) {
    return value * value * value;
  }

  public VolumeUnits () {
    super("volume", true, "m3", "meters^3", "meter^3");
  }

  public final InternationalUnit CUBIC_METER = (InternationalUnit)getBaseUnit();
  public final Unit CUBIC_CENTIMETER = new Unit(CUBIC_METER.CENTI, 1.0, "cc", "cubic_centimeters", "cubic_centimeter");
  public final InternationalUnit LITER = new InternationalUnit(CUBIC_CENTIMETER, 1000.0, "l", "liters", "liter");

  public final static double MILLILITERS_PER_TEASPOON = 236.588 / 48.0;
  public final Unit TEASPOON = new Unit(LITER.MILLI, MILLILITERS_PER_TEASPOON, "tsp", "teaspoons", "teaspoon");

  public final static double TEASPOONS_PER_TABLESPOON = 3.0;
  public final Unit TABLESPOON = new Unit(TEASPOON, TEASPOONS_PER_TABLESPOON, "tbsp", "tablespoons", "tablespoon");

  public final static double TABLESPOONS_PER_FLUID_OUNCE = 2.0;
  public final Unit FLUID_OUNCE = new Unit(TABLESPOON, TABLESPOONS_PER_FLUID_OUNCE, "floz", "fluid_ounces", "fluid_ounce");

  public final static double FLUID_OUNCES_PER_CUP = 8.0;
  public final Unit CUP = new Unit(FLUID_OUNCE, FLUID_OUNCES_PER_CUP, "cup", "cups", "cup");

  public final static double CUPS_PER_PINT = 2.0;
  public final Unit PINT = new Unit(CUP, CUPS_PER_PINT, "pt", "pints", "pint");

  public final static double PINTS_PER_QUART = 2.0;
  public final Unit QUART = new Unit(PINT, PINTS_PER_QUART, "qt", "quarts", "quart");

  public final static double QUARTS_PER_GALLON = 4.0;
  public final Unit GALLON = new Unit(QUART, QUARTS_PER_GALLON, "gal", "gallons", "gallon");

  public final static double CUBIC_CENTIMETERS_PER_CUBIC_INCH = cube(LengthUnits.CENTIMETERS_PER_INCH);
  public final Unit CUBIC_INCH = new Unit(CUBIC_METER.CENTI, CUBIC_CENTIMETERS_PER_CUBIC_INCH, "cbin", "cubic_inches", "cubic_inch");

  public final static double CUBIC_INCHES_PER_CUBIC_FOOT = cube(LengthUnits.INCHES_PER_FOOT);
  public final Unit CUBIC_FOOT = new Unit(CUBIC_INCH, CUBIC_INCHES_PER_CUBIC_FOOT, "cbft", "cubic_feet", "cubic_foot");

  public final static double CUBIC_FEET_PER_CUBIC_YARD = cube(LengthUnits.FEET_PER_YARD);
  public final Unit CUBIC_YARD = new Unit(CUBIC_FOOT, CUBIC_FEET_PER_CUBIC_YARD, "cbyd", "cubic_yards", "cubic_yard");

  public final static double CUBIC_YARDS_PER_CUBIC_ROD = cube(LengthUnits.YARDS_PER_ROD);
  public final Unit CUBIC_ROD = new Unit(CUBIC_YARD, CUBIC_YARDS_PER_CUBIC_ROD, "cbrd", "cubic_rods", "cubic_rod");

  public final static double CUBIC_RODS_PER_CUBIC_FURLONG = cube(LengthUnits.RODS_PER_FURLONG);
  public final Unit CUBIC_FURLONG = new Unit(CUBIC_ROD, CUBIC_RODS_PER_CUBIC_FURLONG, "cbfur", "cubic_furlongs", "cubic_furlong");

  public final static double CUBIC_FURLONGS_PER_CUBIC_MILE = cube(LengthUnits.FURLONGS_PER_MILE);
  public final Unit CUBIC_MILE = new Unit(CUBIC_FURLONG, CUBIC_FURLONGS_PER_CUBIC_MILE, "cbmi", "cubic_miles", "cubic_mile");

  public final static double CUBIC_MILES_PER_CUBIC_LEAGUE = cube(LengthUnits.MILES_PER_LEAGUE);
  public final Unit CUBIC_LEAGUE = new Unit(CUBIC_MILE, CUBIC_MILES_PER_CUBIC_LEAGUE, "cblg", "cubic_leagues", "cubic_league");

  @Override
  public final Unit getDefaultFromUnit () {
    return CUP;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return LITER.MILLI;
  }
}
