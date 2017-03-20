package org.nbp.calculator.conversion;

public class TemperatureUnits extends UnitType {
  public TemperatureUnits () {
    super("temperature", false, "DC", "degrees_celsius", "degree_celsius", "celsius", "centigrade");
  }

  public final Unit CELSIUS = getBaseUnit();
  public final Unit KELVIN = new Unit(CELSIUS, 1, 273.15, "K", "kelvins", "kelvin");
  public final Unit RÉAUMUR = new Unit(CELSIUS, 1.25, "DRé", "degrees_réaumur", "degree_réaumur");
  public final Unit DELISLE = new Unit(CELSIUS, -2.0/3.0, 150.0, "DDe", "degrees_delisle", "degree_delisle");
  public final Unit NEWTON = new Unit(CELSIUS, 100.0/33.0, "DN", "degrees_newton", "degree_newton");
  public final Unit ROMER = new Unit(CELSIUS, 100.0/52.5, 7.5, "DRø", "degrees_rømer", "degree_rømer");

  private final static double RANKINE_PER_KELVIN = 5.0 / 9.0;
  public final Unit FAHRENHEIT = new Unit(CELSIUS, RANKINE_PER_KELVIN, 32, "DF", "degrees_fahrenheit", "degree_fahrenheit", "fahrenheit");
  public final Unit RANKINE = new Unit(KELVIN, RANKINE_PER_KELVIN, "DR", "degrees_rankine", "degree_rankine", "rankine");
}
