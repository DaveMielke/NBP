package org.nbp.calculator.conversion;

public class PressureUnits extends UnitType {
  public PressureUnits () {
    super("pressure", true, "Pa", "pascals", "pascal");
  }

  public final InternationalUnit PASCAL = (InternationalUnit)getBaseUnit();

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
