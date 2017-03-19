package org.nbp.calculator.conversion;

public class AngleUnits extends UnitType {
  public AngleUnits () {
    super("angle", false, "RAD", "radians", "radian");
  }

  public final Unit RADIAN = getBaseUnit();

  public final static double RADIANS_PER_DEGREE = Math.toRadians(1.0);
  public final Unit DEGREE = new Unit(RADIAN, RADIANS_PER_DEGREE, "DEG", "degrees", "degree");

  public final static double DEGREES_PER_GRADIAN = 0.9;
  public final Unit GRADIAN = new Unit(DEGREE, DEGREES_PER_GRADIAN, "GON", "gradians", "gradian", "grads", "grad");
}
