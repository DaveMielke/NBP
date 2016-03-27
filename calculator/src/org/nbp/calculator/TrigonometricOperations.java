package org.nbp.calculator;

public abstract class TrigonometricOperations {
  private TrigonometricOperations () {
  }

  public final static double sin (double x) {
    return Math.sin(x);
  }

  public final static double cos (double x) {
    return Math.cos(x);
  }

  public final static double tan (double x) {
    return Math.tan(x);
  }

  public final static double sec (double x) {
    return 1d / Math.cos(x);
  }

  public final static double csc (double x) {
    return 1d / Math.sin(x);
  }

  public final static double cot (double x) {
    return Math.cos(x) / Math.sin(x);
  }
}
