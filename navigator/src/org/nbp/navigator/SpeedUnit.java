package org.nbp.navigator;

public enum SpeedUnit implements Unit {
  MPS("mps", 1f),
  FPS("fps", FEET_PER_METER),
  KPH("kph", (SECONDS_PER_HOUR / METERS_PER_KILOMETER)),
  MPH("mph", (SECONDS_PER_HOUR * MILES_PER_METER)),
  KN("kn", (SECONDS_PER_HOUR / METERS_PER_KNOT)),
  ; // end of enumeration

  private final String speedSymbol;
  private final float speedMultiplier;

  private SpeedUnit (String symbol, float multiplier) {
    speedSymbol = symbol;
    speedMultiplier = multiplier;
  }

  @Override
  public final String getSymbol () {
    return speedSymbol;
  }

  @Override
  public final float getMultiplier () {
    return speedMultiplier;
  }
}
