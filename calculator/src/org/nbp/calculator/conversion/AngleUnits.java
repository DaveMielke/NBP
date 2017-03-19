package org.nbp.calculator.conversion;

public class AngleUnits extends UnitType {
  public AngleUnits () {
    super("angle", false, "rad", "radians", "radian");
  }

  public final Unit RADIAN = getBaseUnit();

  public final static double RADIANS_PER_BINARY_RADIAN = 1.0 / 128.0;
  public final Unit BINARY_RADIAN = new Unit(RADIAN, RADIANS_PER_BINARY_RADIAN, "brad", "binaryradians", "binaryradian");

  public final static double RADIANS_PER_DEGREE = Math.toRadians(1.0);
  public final Unit DEGREE = new Unit(RADIAN, RADIANS_PER_DEGREE, "deg", "degrees", "degree");

  public final static double DEGREES_PER_REVOLUTION = 360.0;
  public final Unit REVOLUTION = new Unit(DEGREE, DEGREES_PER_REVOLUTION, "rev", "revolutions", "revolution", "turns", "turn");

  public final static double DEGREES_PER_CENTI_REVOLUTION = 3.6;
  public final Unit CENTI_REVOLUTION = new Unit(DEGREE, DEGREES_PER_CENTI_REVOLUTION, "crev", "centirevolutions", "centirevolution", "centiturns", "centiturn");

  public final static double CENTI_REVOLUTIONS_PER_MILLI_REVOLUTION = 0.1;
  public final Unit MILLI_REVOLUTION = new Unit(CENTI_REVOLUTION, CENTI_REVOLUTIONS_PER_MILLI_REVOLUTION, "mrev", "millirevolutions", "millirevolution", "milliturns", "milliturn");

  public final static double DEGREES_PER_QUADRANT = 90.0;
  public final Unit QUADRANT = new Unit(DEGREE, DEGREES_PER_QUADRANT, "quad", "quadrants", "quadrant");

  public final static double DEGREES_PER_SEXTANT = 60.0;
  public final Unit SEXTANT = new Unit(DEGREE, DEGREES_PER_SEXTANT, "sex", "sextants", "sextant");

  public final static double DEGREES_PER_OCTANT = 45.0;
  public final Unit OCTANT = new Unit(DEGREE, DEGREES_PER_OCTANT, "oct", "octants", "octant");

  public final static double DEGREES_PER_SIGN = 30.0;
  public final Unit SIGN = new Unit(DEGREE, DEGREES_PER_SIGN, "sgn", "signs", "sign");

  public final static double DEGREES_PER_POINT = 22.5;
  public final Unit POINT = new Unit(DEGREE, DEGREES_PER_POINT, "pt", "points", "point");

  public final static double DEGREES_PER_HEXACONTADE = 6.0;
  public final Unit HEXACONTADE = new Unit(DEGREE, DEGREES_PER_HEXACONTADE, "hxct", "hexacontades", "hexacontade");

  public final static double DEGREES_PER_ARC_MINUTE = 1.0 / 60.0;
  public final Unit ARC_MINUTE = new Unit(DEGREE, DEGREES_PER_ARC_MINUTE, "moa", "arcminutes", "arcminute");

  public final static double ARC_MINUTES_PER_ARC_SECOND = 1.0 / 60.0;
  public final Unit ARC_SECOND = new Unit(ARC_MINUTE, ARC_MINUTES_PER_ARC_SECOND, "soa", "arcseconds", "arcsecond");

  public final static double DEGREES_PER_GRADIAN = 0.9;
  public final Unit GRADIAN = new Unit(DEGREE, DEGREES_PER_GRADIAN, "gon", "gradians", "gradian", "grads", "grad");

  public final static double GRADIANS_PER_CENTECIMAL_MINUTE = 0.01;
  public final Unit CENTECIMAL_MINUTE = new Unit(GRADIAN, GRADIANS_PER_CENTECIMAL_MINUTE, "cmin", "centecimalminutes", "centecimalminute");

  public final static double CENTECIMAL_MINUTES_PER_CENTECIMAL_SECOND = 0.01;
  public final Unit CENTECIMAL_SECOND = new Unit(CENTECIMAL_MINUTE, CENTECIMAL_MINUTES_PER_CENTECIMAL_SECOND, "csec", "centecimalseconds", "centecimalsecond");
}
