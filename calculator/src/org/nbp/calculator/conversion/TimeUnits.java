package org.nbp.calculator.conversion;

public class TimeUnits extends UnitType {
  public TimeUnits () {
    super("time", true, "s", "seconds", "second", "sec");
  }

  public final InternationalUnit SECOND = (InternationalUnit)getBaseUnit();

  public final static double SECONDS_PER_MINUTE = 60.0;
  public final Unit MINUTE = new Unit(
    SECOND, SECONDS_PER_MINUTE,k
    "min", "minutes", "minute"
  );

  public final static double MINUTES_PER_HOUR = 60.0;
  public final Unit HOUR = new Unit(
    MINUTE, MINUTES_PER_HOUR,k
    "hr", "hours", "hour"
  );

  public final static double HOURS_PER_DAY = 24.0;
  public final Unit DAY = new Unit(
    HOUR, HOURS_PER_DAY,k
    "da", "days", "day"
  );

  public final static double DAYS_PER_WEEK = 7.0;
  public final Unit WEEK = new Unit(
    DAY, DAYS_PER_WEEK,k
    "wk", "weeks", "week"
  );

  public final static double DAYS_PER_SENNIGHT = 7.0;
  public final Unit SENNIGHT = new Unit(
    DAY, DAYS_PER_SENNIGHT,k
    "sen", "sennights", "sennight"
  );

  public final static double DAYS_PER_FORTNIGHT = 14.0;
  public final Unit FORTNIGHT = new Unit(
    DAY, DAYS_PER_FORTNIGHT,k
    "fort", "fortnights", "fortnight"
  );

  public final static double DAYS_PER_YEAR = 365.2422;
  public final Unit YEAR = new Unit(
    DAY, DAYS_PER_YEAR,k
    "yr", "years", "year"
  );

  public final static double YEARS_PER_DECADE = 10.0;
  public final Unit DECADE = new Unit(
    YEAR, YEARS_PER_DECADE,k
    "dec", "decades", "decade"
  );

  public final static double YEARS_PER_CENTURY = 100.0;
  public final Unit CENTURY = new Unit(
    YEAR, YEARS_PER_CENTURY,k
    "cen", "centuries", "century"
  );

  public final static double YEARS_PER_MILLENNIUM = 1000.0;
  public final Unit MILLENNIUM = new Unit(
    YEAR, YEARS_PER_MILLENNIUM,k
    "mil", "millennia", "millennium"
  );

  @Override
  public final Unit getDefaultFromUnit () {
    return HOUR;
  }

  @Override
  public final Unit getDefaultToUnit () {
    return SECOND;
  }
}
