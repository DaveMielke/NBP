package org.nbp.calculator;

public abstract class TrigonometricFunction extends Function {
  @Override
  protected double preprocessArgument (double argument) {
    argument = super.preprocessArgument(argument);

    if (SavedSettings.getDegrees()) {
      argument = Math.toRadians(argument);
    }

    return argument;
  }

  public TrigonometricFunction () {
    super();
  }
}
