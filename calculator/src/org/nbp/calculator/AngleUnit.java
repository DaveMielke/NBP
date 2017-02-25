package org.nbp.calculator;

public enum AngleUnit {
  RADIANS("RAD", "Radians"),
  DEGREES("DEG", "Degrees");

  private final String unitLabel;
  private final String unitDescription;

  public final String getLabel () {
    return unitLabel;
  }

  public final String getDescription () {
    return unitDescription;
  }

  private AngleUnit (String label, String description) {
    unitLabel = label;
    unitDescription = description;
  }
}
