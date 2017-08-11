package org.nbp.navigator;

public enum AngleUnit implements Unit {
  DEGREES("°", 1f),
  GRADIANS("G", (GRADIANS_PER_FULL_TURN / DEGREES_PER_FULL_TURN)),
  MILS("mil", (MILS_PER_FULL_TURN / DEGREES_PER_FULL_TURN)),
  ; // end of enumeration

  private final String angleSymbol;
  private final float angleMultiplier;

  private AngleUnit (String symbol, float multiplier) {
    angleSymbol = symbol;
    angleMultiplier = multiplier;
  }

  @Override
  public final String getSymbol () {
    return angleSymbol;
  }

  @Override
  public final float getMultiplier () {
    return angleMultiplier;
  }
}
