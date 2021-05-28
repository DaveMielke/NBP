package org.nbp.calculator.conversion;

public class PressureUnits extends UnitType {
  public PressureUnits () {
    super("pressure", true, "Pa", "pascals", "pascal");
  }

  public final InternationalUnit PASCAL = (InternationalUnit)getBaseUnit();

  public final Unit TECHNICAL_ATMOSPHERE = new Unit(
    PASCAL.KILO, 98.0665,
    "at", "technical_atmospheres", "technical_atmosphere"
  );

  public final Unit STANDARD_ATMOSPHERE = new Unit(
    PASCAL, 101325,
    "atm", "standard_atmospheres", "standard_atmosphere"
  );

  public final Unit TORR = new Unit(
    STANDARD_ATMOSPHERE, 1.0 / 760.0,
    "Torr", "torrs", "torr"
  );

  public final Unit BAR = new Unit(
    PASCAL, 100000,
    "bar", "bars", "bar"
  );

  public final Unit PSI = new Unit(
    PASCAL.KILO, 6.894757,
    "psi", "pounds_per_square_inch", "pound_per_square_inch"
  );

  public final Unit MILLIMETERS_OF_MERCURY = new Unit(
    PASCAL, 133.322387415,
    "mmHg", "millimeters_of_mercury", "millimeter_of_mercury"
  );

  public final Unit INCHES_OF_MERCURY = new Unit(
    MILLIMETERS_OF_MERCURY, LengthUnits.CENTIMETERS_PER_INCH * 10.0,
    "inHg", "inches_of_mercury", "inch_of_mercury"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return INCHES_OF_MERCURY;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return PASCAL.KILO;
  }
}
