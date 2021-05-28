package org.nbp.calculator;

public class PredefinedConstant extends PredefinedVariable {
  private final AbstractNumber variableValue;

  public PredefinedConstant (String name, AbstractNumber value, String description) {
    super(name, description);
    variableValue = value;
  }

  @Override
  public final AbstractNumber getValue () {
    return variableValue;
  }
}
