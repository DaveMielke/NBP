package org.nbp.calculator;

public abstract class RealOperations {
  private RealOperations () {
  }

  public final static double abs (double x) {
    return Math.abs(x);
  }

  public final static double floor (double x) {
    return Math.floor(x);
  }

  public final static double round (double x) {
    return Math.rint(x);
  }

  public final static double ceil (double x) {
    return Math.ceil(x);
  }

  public final static double sqrt (double x) {
    return Math.sqrt(x);
  }

  public final static double cbrt (double x) {
    return Math.cbrt(x);
  }

  public final static double exp (double x) {
    return Math.exp(x);
  }

  public final static double log (double x) {
    return Math.log(x);
  }

  public final static double log10 (double x) {
    return Math.log10(x);
  }

  public final static double rd2dg (double radians) {
    return Math.toDegrees(radians);
  }

  public final static double dg2rd (double degrees) {
    return Math.toRadians(degrees);
  }

  public final static double sinh (double x) {
    return Math.sinh(x);
  }

  public final static double cosh (double x) {
    return Math.cosh(x);
  }

  public final static double tanh (double x) {
    return Math.tanh(x);
  }

  public final static double sech (double x) {
    return 1d / Math.cosh(x);
  }

  public final static double csch (double x) {
    return 1d / Math.sinh(x);
  }

  public final static double coth (double x) {
    return Math.cosh(x) / Math.sinh(x);
  }
}
