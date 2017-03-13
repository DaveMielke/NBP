package org.nbp.calculator;

public abstract class Units {
  private Units () {
  }

  public final static UnitType TEMPERATURE = new UnitType("temperature", "C", "celsius", "centigrade");
  static {
    Unit celsius = TEMPERATURE.getBaseUnit();
    Unit fahrenheit = new Unit(celsius, 1.8, 32, "F", "fahrenheit");
  }
}
