package org.nbp.calculator;

public class SystemVariable {
  private final GenericNumber value;
  private final String description;

  public final GenericNumber getValue () {
    return value;
  }

  public final String getDescription () {
    return description;
  }

  public SystemVariable (GenericNumber value, String description) {
    this.value = value;
    this.description = description;
  }
}
