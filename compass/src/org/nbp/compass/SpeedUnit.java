package org.nbp.compass;

public enum SpeedUnit {
  KILOMETERS_PER_HOUR("kph", (3600f / 1000f)),
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
