package org.nbp.calculator;

public abstract class InverseTrigonometricFunction extends Function {
  @Override
  protected double postprocessResult (double result) {
    if (SavedSettings.getDegrees()) {
      result = Math.toDegrees(result);
    }

    return super.postprocessResult(result);
  }

  public InverseTrigonometricFunction () {
    super();
  }
}
