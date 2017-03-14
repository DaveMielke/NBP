package org.nbp.calculator.conversion;

public class Temperature extends UnitType {
  public Temperature () {
    super("temperature", "C", "celsius", "centigrade");
  }

  public final Unit CELSIUS = getBaseUnit();
  public final Unit KELVIN = new Unit(CELSIUS, 1, 273.15, "K", "kelvin");
  public final Unit FAHRENHEIT = new Unit(CELSIUS, 1.8, 32, "F", "fahrenheit");
  public final Unit RANKINE = new Unit(KELVIN, 1.8, "R", "rankine");
  public final Unit RÉAUMUR = new Unit(CELSIUS, 0.8, "RÉ", "réaumur");
}
