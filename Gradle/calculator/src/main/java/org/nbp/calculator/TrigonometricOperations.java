package org.nbp.calculator;

public class TrigonometricOperations extends RealOperations {
  public TrigonometricOperations () {
    super();
  }

  @Override
  public Class<? extends Function> getFunctionType () {
    return TrigonometricFunction.class;
  }

  @FunctionMethod(
    summary = "trigonometric sine"
  )

  public final static double sin (double radians) {
    return Math.sin(radians);
  }

  @FunctionMethod(
    summary = "trigonometric cosine"
  )

  public final static double cos (double radians) {
    return Math.cos(radians);
  }

  @FunctionMethod(
    summary = "trigonometric tangent"
  )

  public final static double tan (double radians) {
    return Math.tan(radians);
  }

  @FunctionMethod(
    summary = "trigonometric secant"
  )

  public final static double sec (double radians) {
    return 1d / Math.cos(radians);
  }

  @FunctionMethod(
    summary = "trigonometric cosecant"
  )

  public final static double csc (double radians) {
    return 1d / Math.sin(radians);
  }

  @FunctionMethod(
    summary = "trigonometric cotangent"
  )

  public final static double cot (double radians) {
    return 1d / Math.tan(radians);
  }
}
