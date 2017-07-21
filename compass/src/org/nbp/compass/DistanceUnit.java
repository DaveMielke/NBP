package org.nbp.compass;

public enum DistanceUnit implements UnitDefinitions {
  METERS("M", 1f),
  FEET("ft", FEET_PER_METER),
  ;

  private final String distanceAcronym;
  private final float distanceConversion;

  private DistanceUnit (String acronym, float conversion) {
    distanceAcronym = acronym;
    distanceConversion = conversion;
  }

  public final String getAcronym () {
    return distanceAcronym;
  }

  public final float getConversion () {
    return distanceConversion;
  }
}
