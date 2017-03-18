package org.nbp.calculator.conversion;

public class Time extends UnitType {
  public Time () {
    super("time", true, "s", "seconds", "second", "sec");
  }

  public final Unit SECOND = getBaseUnit();

  public final static double SECONDS_PER_MINUTE = 60.0;
  public final Unit MINUTE = new Unit(SECOND, SECONDS_PER_MINUTE, "mn", "minutes", "minute", "min");

  public final static double MINUTES_PER_HOUR = 60.0;
  public final Unit HOUR = new Unit(MINUTE, MINUTES_PER_HOUR, "hr", "hours", "hour");

  public final static double HOURS_PER_DAY = 24.0;
  public final Unit DAY = new Unit(HOUR, HOURS_PER_DAY, "da", "days", "day");

  public final static double DAYS_PER_WEEK = 7.0;
  public final Unit WEEK = new Unit(DAY, DAYS_PER_WEEK, "wk", "weeks", "week");

  public final static double DAYS_PER_SENNIGHT = 7.0;
  public final Unit SENNIGHT = new Unit(DAY, DAYS_PER_SENNIGHT, "sn", "sennights", "sennight");

  public final static double DAYS_PER_FORTNIGHT = 14.0;
  public final Unit FORTNIGHT = new Unit(DAY, DAYS_PER_FORTNIGHT, "fn", "fortnights", "fortnight");

  public final static double DAYS_PER_YEAR = 365.2422;
  public final Unit YEAR = new Unit(DAY, DAYS_PER_YEAR, "yr", "years", "year");

  public final static double YEARS_PER_DECADE = 10.0;
  public final Unit DECADE = new Unit(YEAR, YEARS_PER_DECADE, "dec", "decades", "decade");

  public final static double YEARS_PER_CENTURY = 100.0;
  public final Unit CENTURY = new Unit(YEAR, YEARS_PER_CENTURY, "cen", "centuries", "century");

  public final static double YEARS_PER_MILLENNIUM = 1000.0;
  public final Unit MILLENNIUM = new Unit(YEAR, YEARS_PER_MILLENNIUM, "mil", "millennia", "millennium");
}
