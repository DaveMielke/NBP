package org.nbp.calculator.conversion;

public class AngleUnits extends UnitType {
  public AngleUnits () {
    super("angle", false, "rad", "radians", "radian");
  }

  public final Unit RADIAN = getBaseUnit();

  public final static double RADIANS_PER_BINARY_RADIAN = 1.0 / 128.0;
  public final Unit BINARY_RADIAN = new Unit(RADIAN, RADIANS_PER_BINARY_RADIAN, "brad", "binary_radians", "binary_radian");

  public final static double RADIANS_PER_DEGREE = Math.toRadians(1.0);
  public final Unit DEGREE = new Unit(RADIAN, RADIANS_PER_DEGREE, "deg", "degrees", "degree");

  public final static double DEGREES_PER_REVOLUTION = 360.0;
  public final Unit REVOLUTION = new Unit(DEGREE, DEGREES_PER_REVOLUTION, "rev", "revolutions", "revolution", "turns", "turn");

  public final static double DEGREES_PER_CENTIREVOLUTION = 3.6;
  public final Unit CENTIREVOLUTION = new Unit(DEGREE, DEGREES_PER_CENTIREVOLUTION, "crev", "centirevolutions", "centirevolution", "centiturns", "centiturn");

  public final static double CENTIREVOLUTIONS_PER_MILLIREVOLUTION = 0.1;
  public final Unit MILLIREVOLUTION = new Unit(CENTIREVOLUTION, CENTIREVOLUTIONS_PER_MILLIREVOLUTION, "mrev", "millirevolutions", "millirevolution", "milliturns", "milliturn");

  public final static double DEGREES_PER_QUADRANT = 90.0;
  public final Unit QUADRANT = new Unit(DEGREE, DEGREES_PER_QUADRANT, "quad", "quadrants", "quadrant");

  public final static double DEGREES_PER_SEXTANT = 60.0;
  public final Unit SEXTANT = new Unit(DEGREE, DEGREES_PER_SEXTANT, "sxt", "sextants", "sextant");

  public final static double DEGREES_PER_OCTANT = 45.0;
  public final Unit OCTANT = new Unit(DEGREE, DEGREES_PER_OCTANT, "oct", "octants", "octant");

  public final static double DEGREES_PER_SIGN = 30.0;
  public final Unit SIGN = new Unit(DEGREE, DEGREES_PER_SIGN, "sign", "signs", "sign");

  public final static double DEGREES_PER_POINT = 11.25;
  public final Unit POINT = new Unit(DEGREE, DEGREES_PER_POINT, "point", "points", "point");

  public final static double DEGREES_PER_HEXACONTADE = 6.0;
  public final Unit HEXACONTADE = new Unit(DEGREE, DEGREES_PER_HEXACONTADE, "hxct", "hexacontades", "hexacontade");

  public final static double DEGREES_PER_ARC_MINUTE = 1.0 / 60.0;
  public final Unit ARC_MINUTE = new Unit(DEGREE, DEGREES_PER_ARC_MINUTE, "dmin", "arc_minutes", "arc_minute");

  public final static double ARC_MINUTES_PER_ARC_SECOND = 1.0 / 60.0;
  public final Unit ARC_SECOND = new Unit(ARC_MINUTE, ARC_MINUTES_PER_ARC_SECOND, "dsec", "arc_seconds", "arc_second");

  public final static double DEGREES_PER_GRADIAN = 0.9;
  public final Unit GRADIAN = new Unit(DEGREE, DEGREES_PER_GRADIAN, "gon", "gradians", "gradian", "grads", "grad");

  public final static double GRADIANS_PER_CENTESIMAL_MINUTE = 0.01;
  public final Unit CENTESIMAL_MINUTE = new Unit(GRADIAN, GRADIANS_PER_CENTESIMAL_MINUTE, "gmin", "centesimal_minutes", "centesimal_minute");

  public final static double CENTESIMAL_MINUTES_PER_CENTESIMAL_SECOND = 0.01;
  public final Unit CENTESIMAL_SECOND = new Unit(CENTESIMAL_MINUTE, CENTESIMAL_MINUTES_PER_CENTESIMAL_SECOND, "gsec", "centesimal_seconds", "centesimal_second");

  @Override
  public final Unit getDefaultFromUnit () {
    return DEGREE;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return RADIAN;
  }
}
