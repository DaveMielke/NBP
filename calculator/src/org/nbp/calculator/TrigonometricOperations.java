package org.nbp.calculator;

public abstract class TrigonometricOperations {
  private TrigonometricOperations () {
  }

  public final static double sin (double radians) {
    return Math.sin(radians);
  }

  public final static double cos (double radians) {
    return Math.cos(radians);
  }

  public final static double tan (double radians) {
    return Math.tan(radians);
  }

  public final static double sec (double radians) {
    return 1d / Math.cos(radians);
  }

  public final static double csc (double radians) {
    return 1d / Math.sin(radians);
  }

  public final static double cot (double radians) {
    return 1d / Math.tan(radians);
  }
}
