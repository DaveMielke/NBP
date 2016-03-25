package org.nbp.calculator;

public class ComplexNumber extends ComplexCommon {
  private final double real;
  private final double imag;

  public ComplexNumber (double r, double i) {
    super();

    real = r;
    imag = i;
  }

  public ComplexNumber (double r) {
    this(r, ZERO);
  }

  public final double real () {
    return real;
  }

  public final double imag () {
    return imag;
  }

  public final boolean hasReal () {
    return real != ZERO;
  }

  public final boolean hasImag () {
    return imag != ZERO;
  }

  private final boolean isReal (ComplexNumber operand) {
    return !(hasImag() || operand.hasImag());
  }

  public final static ComplexNumber NaN = new ComplexNumber(Double.NaN, Double.NaN);

  public final boolean isNaN () {
    return Double.isNaN(real) || Double.isNaN(imag);
  }

  public final boolean equals (ComplexNumber operand) {
    if (isNaN()) return operand.isNaN();
    if (operand.isNaN()) return false;
    return ((real == operand.real) && (imag == operand.imag));
  }

  public final ComplexNumber abs () {
    return new ComplexNumber(
      (imag == ZERO)? Math.abs(real):
      (real == ZERO)? Math.abs(imag):
      Math.hypot(real, imag)
    );
  }

  public final ComplexNumber neg () {
    return new ComplexNumber(-real, -imag);
  }

  public final ComplexNumber con () {
    return new ComplexNumber(real, -imag);
  }

  public final ComplexNumber rec () {
    if (imag == ZERO) return new ComplexNumber(1d / real);

    double denominator = (real * real) + (imag * imag);
    return new ComplexNumber((real / denominator), (-imag / denominator));
  }

  public final ComplexNumber add (ComplexNumber addend) {
    return new ComplexNumber((real + addend.real), (imag + addend.imag));
  }

  public final ComplexNumber sub (ComplexNumber subtrahend) {
    return new ComplexNumber((real - subtrahend.real), (imag - subtrahend.imag));
  }

  public final ComplexNumber mul (ComplexNumber multiplier) {
    return new ComplexNumber(
      (real * multiplier.real) - (imag * multiplier.imag),
      (real * multiplier.imag) + (imag * multiplier.real)
    );
  }

  public final ComplexNumber div (ComplexNumber divisor) {
    if (isReal(divisor)) return new ComplexNumber(real / divisor.real);
    return mul(divisor.rec());
  }

  public final ComplexNumber log () {
    if (imag == ZERO) return new ComplexNumber(Math.log(real));

    return new ComplexNumber(
      Math.log(Math.hypot(real, imag)),
      Math.atan2(imag, real)
    );
  }

  public final ComplexNumber exp () {
    if (imag == ZERO) return new ComplexNumber(Math.exp(real));
    double multiplicand = Math.exp(real);

    return new ComplexNumber(
      multiplicand * Math.cos(imag),
      multiplicand * Math.sin(imag)
    );
  }

  public final ComplexNumber pow (ComplexNumber exponent) {
    if (isReal(exponent)) return new ComplexNumber(Math.pow(real, exponent.real));
    return NaN;
  }

  public final String format () {
    return ComplexFormatter.getInstance().format(real, imag);
  }

  private final static char STRING_DELIMITER = '_';

  public final String toString() {
    return (Double.toString(real) + STRING_DELIMITER + Double.toString(imag));
  }

  public final static ComplexNumber valueOf (String string) {
    int delimiter = string.indexOf(STRING_DELIMITER);
    if (delimiter < 0) return new ComplexNumber(Double.valueOf(string));

    return new ComplexNumber(
      Double.valueOf(string.substring(0, delimiter)),
      Double.valueOf(string.substring(delimiter+1))
    );
  }
}
