package org.nbp.calculator.conversion;

public class SpeedUnits extends UnitType {
  public SpeedUnits () {
    super("speed", true, "mps", "meters_per_second", "meter_per_second");
  }

  public final InternationalUnit METERS_PER_SECOND = (InternationalUnit)getBaseUnit();

  public final Unit KILOMETERS_PER_HOUR = new Unit(
    METERS_PER_SECOND.KILO, 1.0 / (TimeUnits.SECONDS_PER_MINUTE * TimeUnits.MINUTES_PER_HOUR),
    "kmph", "kilometers_per_hour", "kilometer_per_hour"
  );

  public final Unit MILES_PER_HOUR = new Unit(
    KILOMETERS_PER_HOUR, (LengthUnits.CENTIMETERS_PER_INCH * 12.0 * 5280.0) / 100000,
    "mph", "miles_per_hour", "mile_per_hour"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return MILES_PER_HOUR;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return KILOMETERS_PER_HOUR;
  }
}
