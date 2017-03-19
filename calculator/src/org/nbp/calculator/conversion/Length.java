package org.nbp.calculator.conversion;

public class Length extends UnitType {
  public Length () {
    super("length", true, "m", "meters", "meter");
  }

  public final InternationalUnit METER = (InternationalUnit)getBaseUnit();

  public final static double METERS_PER_INCH = 0.0254;
  public final Unit INCH = new Unit(METER, METERS_PER_INCH, "in", "inches", "inch");

  public final static double INCHES_PER_FOOT = 12.0;
  public final Unit FOOT = new Unit(INCH, INCHES_PER_FOOT, "ft", "feet", "foot");

  public final static double FEET_PER_YARD = 3.0;
  public final Unit YARD = new Unit(FOOT, FEET_PER_YARD, "yd", "yards", "yard");

  public final static double YARDS_PER_ROD = 5.5;
  public final Unit ROD = new Unit(YARD, YARDS_PER_ROD, "rd", "rods", "rod");

  public final static double RODS_PER_FURLONG = 40.0;
  public final Unit FURLONG = new Unit(ROD, RODS_PER_FURLONG, "fur", "furlongs", "furlong");

  public final static double FURLONGS_PER_MILE = 8.0;
  public final Unit MILE = new Unit(FURLONG, FURLONGS_PER_MILE, "mi", "miles", "mile");

  public final static double MILES_PER_LEAGUE = 3.0;
  public final Unit LEAGUE = new Unit(MILE, MILES_PER_LEAGUE, "lg", "leagues", "league");
}
