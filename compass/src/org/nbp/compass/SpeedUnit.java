package org.nbp.compass;

public enum SpeedUnit implements Unit {
  MPS("mps", 1f),
  FPS("fps", FEET_PER_METER),
  KPH("kph", (SECONDS_PER_HOUR * KILOMETERS_PER_METER)),
  MPH("mph", (SECONDS_PER_HOUR * MILES_PER_METER)),
  KN("kn", (SECONDS_PER_HOUR / METERS_PER_KNOT)),
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
