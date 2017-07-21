package org.nbp.compass;

public enum SpeedUnit implements Unit {
  MPS("mps", 1f),
  KPH("kph", (KILOMETERS_PER_METER * SECONDS_PER_HOUR)),
  MPH("mph", (MILES_PER_METER * SECONDS_PER_HOUR)),
  ;

  private final String speedAcronym;
  private final float speedConversion;

  private SpeedUnit (String acronym, float conversion) {
    speedAcronym = acronym;
    speedConversion = conversion;
  }

  @Override
  public final String getAcronym () {
    return speedAcronym;
  }

  @Override
  public final float getConversion () {
    return speedConversion;
  }
}
