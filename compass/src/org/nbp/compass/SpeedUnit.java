package org.nbp.compass;

public enum SpeedUnit implements UnitDefinitions {
  KPH("kph", (KILOMETERS_PER_METER * SECONDS_PER_HOUR)),
  MPH("mph", (MILES_PER_METER * SECONDS_PER_HOUR)),
  ;

  private final String speedAcronym;
  private final float speedConversion;

  private SpeedUnit (String acronym, float conversion) {
    speedAcronym = acronym;
    speedConversion = conversion;
  }

  public final String getAcronym () {
    return speedAcronym;
  }

  public final float getConversion () {
    return speedConversion;
  }
}
