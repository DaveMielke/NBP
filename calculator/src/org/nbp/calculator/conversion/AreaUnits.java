package org.nbp.calculator.conversion;

public class AreaUnits extends UnitType {
  private final static double square (double value) {
    return value * value;
  }

  public AreaUnits () {
    super("area", true, "m2", "meters2", "meter2");
  }

  public final InternationalUnit SQUARE_METER = (InternationalUnit)getBaseUnit();
  public final Unit ARE = new Unit(SQUARE_METER, 100.0, "a", "are");
  public final Unit HECTARE = new Unit(ARE, 100.0, "ha", "hectare");

  public final static double SQUARE_METERS_PER_SQUARE_INCH = square(LengthUnits.METERS_PER_INCH);
  public final Unit SQUARE_INCH = new Unit(SQUARE_METER, SQUARE_METERS_PER_SQUARE_INCH, "sqin", "squareinches", "squareinch");

  public final static double SQUARE_INCHES_PER_SQUARE_FOOT = square(LengthUnits.INCHES_PER_FOOT);
  public final Unit SQUARE_FOOT = new Unit(SQUARE_INCH, SQUARE_INCHES_PER_SQUARE_FOOT, "sqft", "squarefeet", "squarefoot");

  public final static double SQUARE_FEET_PER_SQUARE_YARD = square(LengthUnits.FEET_PER_YARD);
  public final Unit SQUARE_YARD = new Unit(SQUARE_FOOT, SQUARE_FEET_PER_SQUARE_YARD, "sqyd", "squareyards", "squareyard");

  public final static double SQUARE_YARDS_PER_SQUARE_ROD = square(LengthUnits.YARDS_PER_ROD);
  public final Unit SQUARE_ROD = new Unit(SQUARE_YARD, SQUARE_YARDS_PER_SQUARE_ROD, "sqrd", "squarerods", "squarerod");

  public final static double SQUARE_RODS_PER_SQUARE_FURLONG = square(LengthUnits.RODS_PER_FURLONG);
  public final Unit SQUARE_FURLONG = new Unit(SQUARE_ROD, SQUARE_RODS_PER_SQUARE_FURLONG, "sqfur", "squarefurlongs", "squarefurlong");

  public final static double SQUARE_FURLONGS_PER_SQUARE_MILE = square(LengthUnits.FURLONGS_PER_MILE);
  public final Unit SQUARE_MILE = new Unit(SQUARE_FURLONG, SQUARE_FURLONGS_PER_SQUARE_MILE, "sqmi", "squaremiles", "squaremile");

  public final static double SQUARE_MILES_PER_SQUARE_LEAGUE = square(LengthUnits.MILES_PER_LEAGUE);
  public final Unit SQUARE_LEAGUE = new Unit(SQUARE_MILE, SQUARE_MILES_PER_SQUARE_LEAGUE, "sqlg", "squareleagues", "squareleague");

  public final static double SQUARE_RODS_PER_ROOD = 40.0;
  public final Unit ROOD = new Unit(SQUARE_ROD, SQUARE_RODS_PER_ROOD, "ro", "roods", "rood");

  public final static double ROODS_PER_ACRE = 4.0;
  public final Unit ACRE = new Unit(ROOD, ROODS_PER_ACRE, "ac", "acres", "acre");
}
