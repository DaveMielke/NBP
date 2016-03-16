package org.nbp.calculator;

public abstract class TrigonometricFunction extends Function {
  @Override
  protected double preprocessArgument (double argument) {
    argument = super.preprocessArgument(argument);

    if (isChecked(R.id.checkbox_degrees)) {
      argument = Math.toRadians(argument);
    }

    return argument;
  }

  public TrigonometricFunction () {
    super();
  }
}
