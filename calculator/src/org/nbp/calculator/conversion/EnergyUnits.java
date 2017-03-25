package org.nbp.calculator.conversion;

public class EnergyUnits extends UnitType {
  public EnergyUnits () {
    super("energy", true, "J", "joules", "joule");
  }

  public final InternationalUnit JOULE = (InternationalUnit)getBaseUnit();

  public final Unit ERG = new Unit(
    JOULE.NANO, 100.0,
    "erg", "ergs", "erg"
  );

  public final Unit RYDBERG = new Unit(
    JOULE.ATTO, 2.179872,
    "Ry", "rydbergs", "rydberg"
  );

  public final Unit ELECTRONVOLT = new Unit(
    JOULE.ZEPTO, 160.217653,
    "eV", "electronvolts", "electronvolt"
  );

  public final Unit HARTREE = new Unit(
    ELECTRONVOLT, 27.2107,
    "Ha", "hartrees", "hartree"
  );

  public final Unit FOOT_POUND_FORCE = new Unit(
    JOULE, 1.3558,
    "ftlbf", "foot_pounds_force", "foot_pound_force"
  );

  public final Unit BRITISH_THERMAL_UNIT = new Unit(
    JOULE, 1055.0,
    "BTU", "British_thermal_units", "British_thermal_unit"
  );

  public final Unit THERM = new Unit(
    BRITISH_THERMAL_UNIT, 100000.0,
    "thm", "therms", "therm"
  );

  public final Unit HORSEPOWER_HOUR = new Unit(
    JOULE.MEGA, 2.6845,
    "hphr", "horsepower_hours", "horsepower_hour"
  );

  public final Unit GASOLINE_GALLON = new Unit(
    JOULE.MEGA, 120.0,
    "gasgal", "gasoline_gallons", "gasoline_gallon"
  );

  public final Unit KILOWATT_HOUR = new Unit(
    JOULE.MEGA, 3.6,
    "kWh", "kilowatt_hours", "kilowatt_hour"
  );

  public final Unit SMALL_CALORIE = new Unit(
    JOULE, 4.184,
    "cal", "small_calories", "small_calorie"
  );

  public final Unit LARGE_CALORIE = new Unit(
    SMALL_CALORIE, 1000.0,
    "Cal", "large_calories", "large_calorie"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return LARGE_CALORIE;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return JOULE;
  }
}
