package org.nbp.calculator;

import java.lang.reflect.Method;

public class InverseTrigonometricFunction extends RealFunction {
  @Override
  protected double postprocessRealResult (double result) {
    result = SavedSettings.getAngleUnit().getConverter().fromRadians(result);
    result = super.postprocessRealResult(result);
    return result;
  }

  public InverseTrigonometricFunction (Method method) {
    super(method);
  }
}
