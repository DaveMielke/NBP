package org.nbp.calculator.conversion;

public abstract class Temperature {
  private Temperature () {
  }

  public final static UnitType TEMPERATURE = new UnitType("temperature", "C", "celsius", "centigrade");
  public final static Unit CELSIUS = TEMPERATURE.getBaseUnit();
  public final static Unit KELVIN = new Unit(CELSIUS, 1, 273.15, "K", "kelvin");
  public final static Unit FAHRENHEIT = new Unit(CELSIUS, 1.8, 32, "F", "fahrenheit");
  public final static Unit RANKINE = new Unit(KELVIN, 1.8, "R", "rankine");
  public final static Unit RÉAUMUR = new Unit(CELSIUS, 0.8, "RÉ", "réaumur");
}
