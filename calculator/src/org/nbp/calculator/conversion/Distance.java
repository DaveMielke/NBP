package org.nbp.calculator.conversion;

public class Distance extends UnitType {
  public Distance () {
    super("distance", true, "m", "meters", "meter");
  }

  public final Unit METER = getBaseUnit();
  public final Unit INCH = new Unit(METER, 1.0/39.37, "in", "inches", "inch");
  public final Unit FOOT = new Unit(INCH, 12, "ft", "feet", "foot");
  public final Unit YARD = new Unit(FOOT, 3, "yd", "yards", "yard");
  public final Unit ROD = new Unit(YARD, 5.5, "rd", "rods", "rod");
  public final Unit FURLONG = new Unit(ROD, 40, null, "furlongs", "furlong");
  public final Unit MILE = new Unit(FURLONG, 8, "mi", "miles", "mile");
  public final Unit LEAGUE = new Unit(MILE, 3, null, "leagues", "league");
}
