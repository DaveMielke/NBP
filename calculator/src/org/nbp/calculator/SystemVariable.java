package org.nbp.calculator;

public class SystemVariable extends Variable {
  private final AbstractNumber variableValue;
  private final String variableDescription;

  public SystemVariable (String name, AbstractNumber value, String description) {
    super(name);
    variableValue = value;
    variableDescription = description;
  }

  @Override
  public final AbstractNumber getValue () {
    return variableValue;
  }

  @Override
  public final String getDescription () {
    return variableDescription;
  }
}
