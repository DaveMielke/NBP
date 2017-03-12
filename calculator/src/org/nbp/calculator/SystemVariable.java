package org.nbp.calculator;

public class SystemVariable extends Variable {
  private final GenericNumber variableValue;
  private final String variableDescription;

  public SystemVariable (String name, GenericNumber value, String description) {
    super(name);
    variableValue = value;
    variableDescription = description;
  }

  @Override
  public final GenericNumber getValue () {
    return variableValue;
  }

  @Override
  public final String getDescription () {
    return variableDescription;
  }
}
