package org.nbp.calculator;

import java.lang.reflect.Method;

public class TrigonometricFunction extends Function {
  @Override
  protected double preprocessArgument (double argument) {
    argument = super.preprocessArgument(argument);

    if (SavedSettings.getDegrees()) {
      argument = Math.toRadians(argument);
    }

    return argument;
  }

  public TrigonometricFunction (Method method) {
    super(method);
  }
}
