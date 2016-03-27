package org.nbp.calculator;

public class RealOperations extends Operations {
  public RealOperations () {
    super();
  }

  @Override
  public Class<? extends Function> getFunctionType () {
    return RealFunction.class;
  }

  @Override
  public Class<?> getArgumentType () {
    return double.class;
  }

  @FunctionMethod(
    summary = "absolute value of real number"
  )

  public final static double abs (double x) {
    return Math.abs(x);
  }

  @FunctionMethod(
    summary = "round real number down to nearest integer"
  )

  public final static double floor (double x) {
    return Math.floor(x);
  }

  @FunctionMethod(
    summary = "round real number to nearest integer"
  )

  public final static double round (double x) {
    return Math.rint(x);
  }

  @FunctionMethod(
    summary = "round real number up to nearest integer"
  )

  public final static double ceil (double x) {
    return Math.ceil(x);
  }

  @FunctionMethod(
    summary = "square root of real number"
  )

  public final static double sqrt (double x) {
    return Math.sqrt(x);
  }

  @FunctionMethod(
    summary = "cube root of real number"
  )

  public final static double cbrt (double x) {
    return Math.cbrt(x);
  }

  @FunctionMethod(
    summary = "natural exponential of real number"
  )

  public final static double exp (double x) {
    return Math.exp(x);
  }

  @FunctionMethod(
    summary = "natural log of real number"
  )

  public final static double log (double x) {
    return Math.log(x);
  }

  @FunctionMethod(
    summary = "log base 10 of real number"
  )

  public final static double log10 (double x) {
    return Math.log10(x);
  }

  @FunctionMethod(
    summary = "convert real number from radians to degrees"
  )

  public final static double rd2dg (double radians) {
    return Math.toDegrees(radians);
  }

  @FunctionMethod(
    summary = "convert real number from degrees to radians"
  )

  public final static double dg2rd (double degrees) {
    return Math.toRadians(degrees);
  }

  @FunctionMethod(
    summary = "hyperbolic sine of real number"
  )

  public final static double sinh (double x) {
    return Math.sinh(x);
  }

  @FunctionMethod(
    summary = "hyperbolic cosine of real number"
  )

  public final static double cosh (double x) {
    return Math.cosh(x);
  }

  @FunctionMethod(
    summary = "hyperbolic tangent of real number"
  )

  public final static double tanh (double x) {
    return Math.tanh(x);
  }

  @FunctionMethod(
    summary = "hyperbolic secant of real number"
  )

  public final static double sech (double x) {
    return 1d / Math.cosh(x);
  }

  @FunctionMethod(
    summary = "hyperbolic cosecant of real number"
  )

  public final static double csch (double x) {
    return 1d / Math.sinh(x);
  }

  @FunctionMethod(
    summary = "hyperbolic cotangent of real number"
  )

  public final static double coth (double x) {
    return 1d / Math.tanh(x);
  }
}
