package org.nbp.calculator;

import java.lang.reflect.Method;

public class TrigonometricFunction extends RealFunction {
  @Override
  protected double preprocessRealArgument (double argument) {
    argument = super.preprocessRealArgument(argument);

    if (SavedSettings.getDegrees()) {
      argument = Math.toRadians(argument);
    }

    return argument;
  }

  public TrigonometricFunction (Method method) {
    super(method);
  }
}
