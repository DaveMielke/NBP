package org.nbp.calculator;

public class SystemVariable {
  private final double value;
  private final String description;

  public final double getValue () {
    return value;
  }

  public final String getDescription () {
    return description;
  }

  public SystemVariable (double value, String description) {
    this.value = value;
    this.description = description;
  }
}
