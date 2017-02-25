package org.nbp.calculator;

import java.lang.reflect.Method;

public class InverseTrigonometricFunction extends RealFunction {
  @Override
  protected double postprocessRealResult (double result) {
    if (SavedSettings.getAngleUnit().equals(AngleUnit.DEGREES)) {
      result = Math.toDegrees(result);
    }

    return super.postprocessRealResult(result);
  }

  public InverseTrigonometricFunction (Method method) {
    super(method);
  }
}
