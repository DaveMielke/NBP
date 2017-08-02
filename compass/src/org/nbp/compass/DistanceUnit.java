package org.nbp.compass;

public enum DistanceUnit implements Unit {
  METERS("M", 1f),
  FEET("ft", FEET_PER_METER),
  ; // end of enumeration

  private final String distanceSymbol;
  private final float distanceMultiplier;

  private DistanceUnit (String symbol, float multiplier) {
    distanceSymbol = symbol;
    distanceMultiplier = multiplier;
  }

  @Override
  public final String getSymbol () {
    return distanceSymbol;
  }

  @Override
  public final float getMultiplier () {
    return distanceMultiplier;
  }
}
