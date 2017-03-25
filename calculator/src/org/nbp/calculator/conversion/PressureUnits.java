package org.nbp.calculator.conversion;

public class PressureUnits extends UnitType {
  public PressureUnits () {
    super("pressure", true, "Pa", "pascals", "pascal");
  }

  public final InternationalUnit PASCAL = (InternationalUnit)getBaseUnit();

  public final Unit AT = new Unit(
    PASCAL.KILO, 98.0665,
    "at", "technical atmospheres", "technical atmosphere"
  );

  public final Unit ATM = new Unit(
    PASCAL, 101325,
    "atm", "standard atmospheres", "standard atmosphere"
  );

  public final Unit TORR = new Unit(
    ATM, 1.0 / 760.0,
    "Torr", "torrs", "torr"
  );

  public final Unit BAR = new Unit(
    PASCAL, 100000,
    "bar", "bars", "bar"
  );

  public final Unit PSI = new Unit(
    PASCAL.KILO, 6.894757,
    "psi", "pounds per square inch", "pound per square inch"
  );

  public final Unit MMHG = new Unit(
    PASCAL, 133.322387415,
    "mmHg", "millimeters of mercury", "millimeter of mercury"
  );

  public final Unit INHG = new Unit(
    MMHG, LengthUnits.CENTIMETERS_PER_INCH * 10.0,
    "inHg", "inches of mercury", "inch of mercury"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return INHG;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return PASCAL.KILO;
  }
}
