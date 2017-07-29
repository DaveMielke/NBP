package org.nbp.compass;

public enum AngleUnit implements Unit {
  DEGREES("°", 1f),
  GRADIANS("G", (GRADIANS_PER_FULL_TURN / DEGREES_PER_FULL_TURN)),
  MILS("mil", (MILS_PER_FULL_TURN / DEGREES_PER_FULL_TURN)),
  ;

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
