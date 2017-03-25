package org.nbp.calculator.conversion;

public class ForceUnits extends UnitType {
  public ForceUnits () {
    super("force", true, "N", "newtons", "newton");
  }

  public final InternationalUnit NEWTON = (InternationalUnit)getBaseUnit();

  public final Unit DYNE = new Unit(
    NEWTON.MICRO, 10.0,
    "dyn", "dynes", "dyne"
  );

  public final Unit STHÈNE = new Unit(
    NEWTON.KILO, 1.0,
    "sn", "sthènes", "sthène"
  );

  public final Unit KILOGRAM_FORCE = new Unit(
    NEWTON, 9.80665,
    "kgf", "kilograms-force", "kilogram-force"
  );

  public final Unit POUND_FORCE = new Unit(
    NEWTON, 4.4482216152605,
    "lbf", "pounds-force", "pound-force"
  );

  public final Unit KIP = new Unit(
    POUND_FORCE, 1000.0,
    "kip", "kips", "kip"
  );

  public final Unit POUNDAL = new Unit(
    NEWTON, 0.138254954376,
    "pdl", "poundals", "poundal"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return DYNE;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return NEWTON;
  }
}
