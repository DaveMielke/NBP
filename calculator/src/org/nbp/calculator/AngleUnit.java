package org.nbp.calculator;

public enum AngleUnit {
  RADIANS("RAD", "Radians"),
  DEGREES("DEG", "Degrees");

  private final String unitLabel;
  private final String unitName;

  public final String getLabel () {
    return unitLabel;
  }

  public final String getName () {
    return unitName;
  }

  private AngleUnit (String label, String name) {
    unitLabel = label;
    unitName = name;
  }
}
