package org.nbp.calculator;

public abstract class TrigonometricFunction extends Function {
  @Override
  protected double preprocessArgument (double argument) {
    argument = super.preprocessArgument(argument);

    if (CalculatorSettings.DEGREES) {
      argument = Math.toRadians(argument);
    }

    return argument;
  }

  public TrigonometricFunction () {
    super();
  }
}
