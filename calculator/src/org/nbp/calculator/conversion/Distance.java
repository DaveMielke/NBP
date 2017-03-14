package org.nbp.calculator.conversion;

public class Distance extends UnitType {
  public Distance () {
    super("distance", "meter", "m");
  }

  public final Unit METER = getBaseUnit();
  public final Unit INCH = new Unit(METER, 1.0/39.37, "inch", "in");
  public final Unit FOOT = new Unit(INCH, 12, "foot", "ft");
  public final Unit YARD = new Unit(FOOT, 3, "yard", "yd");
  public final Unit ROD = new Unit(YARD, 5.5, "rod", "rd");
  public final Unit FURLONG = new Unit(ROD, 40, "furlong");
  public final Unit MILE = new Unit(FURLONG, 8, "mile", "mi");
  public final Unit LEAGUE = new Unit(MILE, 3, "league");
}
