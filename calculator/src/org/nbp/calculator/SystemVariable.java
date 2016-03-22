package org.nbp.calculator;

public class SystemVariable {
  private final ComplexNumber value;
  private final String description;

  public final ComplexNumber getValue () {
    return value;
  }

  public final String getDescription () {
    return description;
  }

  public SystemVariable (ComplexNumber value, String description) {
    this.value = value;
    this.description = description;
  }
}
