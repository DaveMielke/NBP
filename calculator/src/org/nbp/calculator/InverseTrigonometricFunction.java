package org.nbp.calculator;

import java.lang.reflect.Method;

public class InverseTrigonometricFunction extends RealFunction {
  @Override
  protected double postprocessResult (double result) {
    if (SavedSettings.getDegrees()) {
      result = Math.toDegrees(result);
    }

    return super.postprocessResult(result);
  }

  public InverseTrigonometricFunction (Method method) {
    super(method);
  }
}
