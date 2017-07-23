package org.nbp.compass;

public enum AngleUnit implements Unit {
  DEGREES("°", 1f),
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
