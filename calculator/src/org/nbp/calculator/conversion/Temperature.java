package org.nbp.calculator.conversion;

public class Temperature extends UnitType {
  public Temperature () {
    super("temperature", "celsius", "C", "centigrade");
  }

  public final Unit CELSIUS = getBaseUnit();
  public final Unit KELVIN = new Unit(CELSIUS, 1, 273.15, "kelvin", "K");
  public final Unit FAHRENHEIT = new Unit(CELSIUS, 1.8, 32, "fahrenheit", "F");
  public final Unit RANKINE = new Unit(KELVIN, 1.8, "rankine", "R");
  public final Unit RÉAUMUR = new Unit(CELSIUS, 0.8, "réaumur", "RÉ");
}
