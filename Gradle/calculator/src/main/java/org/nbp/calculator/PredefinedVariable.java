package org.nbp.calculator;

public abstract class PredefinedVariable extends Variable {
  private final String variableDescription;

  protected PredefinedVariable (String name, String description) {
    super(name);
    variableDescription = description;
  }

  @Override
  public final String getDescription () {
    return variableDescription;
  }
}
