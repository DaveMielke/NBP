package org.nbp.calculator;

public class InverseTrigonometricOperations extends RealOperations {
  public InverseTrigonometricOperations () {
    super();
  }

  @Override
  public Class<? extends ComplexFunction> getFunctionType () {
    return InverseTrigonometricFunction.class;
  }

  public final static double asin (double x) {
    return Math.asin(x);
  }

  public final static double acos (double x) {
    return Math.acos(x);
  }

  public final static double atan (double x) {
    return Math.atan(x);
  }

  public final static double asec (double x) {
    return Math.acos(1d / x);
  }

  public final static double acsc (double x) {
    return Math.asin(1d / x);
  }

  public final static double acot (double x) {
    return Math.atan(1d / x);
  }
}