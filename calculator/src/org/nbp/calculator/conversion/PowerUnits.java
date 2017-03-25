package org.nbp.calculator.conversion;

public class PowerUnits extends UnitType {
  public PowerUnits () {
    super("power", true, "W", "watts", "watt");
  }

  public final InternationalUnit WATT = (InternationalUnit)getBaseUnit();

  public final Unit MECHANICAL_HORSEPOWER = new Unit(
    WATT, 745.69987158,
    "hp", "mechanical_horsepower", "mechanical_horsepower"
  );

  public final Unit BOILER_HORSEPOWER = new Unit(
    WATT, 9809.5,
    "bhp", "boiler_horsepower", "boiler_horsepower"
  );

  public final Unit ELECTRICAL_HORSEPOWER = new Unit(
    WATT, 746.0,
    "ehp", "electrical_horsepower", "electrical_horsepower"
  );

  public final Unit METRIC_HORSEPOWER = new Unit(
    WATT, 735.49875,
    "mhp", "metric_horsepower", "metric_horsepower"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return MECHANICAL_HORSEPOWER;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return WATT;
  }
}
