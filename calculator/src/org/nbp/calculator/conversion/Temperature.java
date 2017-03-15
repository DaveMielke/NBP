package org.nbp.calculator.conversion;

public class Temperature extends UnitType {
  public Temperature () {
    super("temperature", false, "C", "celsius", "centigrade");
  }

  public final Unit CELSIUS = getBaseUnit();
  public final Unit KELVIN = new Unit(CELSIUS, 1, 273.15, "K", "kelvin");
  public final Unit RÉAUMUR = new Unit(CELSIUS, 0.8, "RÉ", "réaumur");

  private final static double RANKINE_PER_KELVIN = 5.0 / 9.0;
  public final Unit FAHRENHEIT = new Unit(CELSIUS, RANKINE_PER_KELVIN, 32, "F", "fahrenheit");
  public final Unit RANKINE = new Unit(KELVIN, RANKINE_PER_KELVIN, "R", "rankine");
}
