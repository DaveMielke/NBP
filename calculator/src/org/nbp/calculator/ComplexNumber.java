package org.nbp.calculator;

public class ComplexNumber extends ComplexCommon {
  private final double real;
  private final double imag;

  public ComplexNumber (double r, double i) {
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

  private final static boolean isReal (ComplexNumber number1, ComplexNumber number2) {
    return !(number1.hasImag() || number2.hasImag());
  }

  public final static ComplexNumber NaN = new ComplexNumber(Double.NaN, Double.NaN);

  public final static boolean isNaN (ComplexNumber number) {
    return Double.isNaN(number.real) || Double.isNaN(number.imag);
  }

  public final boolean isNaN () {
    return isNaN(this);
  }

  public final static boolean equals (ComplexNumber left, ComplexNumber right) {
    if (left.isNaN()) return right.isNaN();
    if (right.isNaN()) return false;
    return ((left.real == right.real) && (left.imag == right.imag));
  }

  public final boolean equals (ComplexNumber right) {
    return equals(this, right);
  }

  public final static ComplexNumber abs (ComplexNumber number) {
    double r = number.real;
    double i = number.imag;

    return new ComplexNumber(
      (i == ZERO)? Math.abs(r):
      (r == ZERO)? Math.abs(i):
      Math.hypot(r, i)
    );
  }

  public final ComplexNumber abs () {
    return abs(this);
  }

  public final static ComplexNumber neg (ComplexNumber number) {
    return new ComplexNumber(-number.real, -number.imag);
  }

  public final ComplexNumber neg () {
    return neg(this);
  }

  public final static ComplexNumber con (ComplexNumber number) {
    return new ComplexNumber(number.real, -number.imag);
  }

  public final ComplexNumber con () {
    return con(this);
  }

  public final static ComplexNumber rec (ComplexNumber number) {
    double r = number.real;
    double i = number.imag;
    if (i == ZERO) return new ComplexNumber(1 / r);

    double d = (r * r) + (i * i);
    return new ComplexNumber((r / d), (-i / d));
  }

  public final ComplexNumber rec () {
    return rec(this);
  }

  public final static ComplexNumber add (ComplexNumber augend, ComplexNumber addend) {
    return new ComplexNumber(
      augend.real + addend.real,
      augend.imag + addend.imag
    );
  }

  public final ComplexNumber add (ComplexNumber addend) {
    return add(this, addend);
  }

  public final static ComplexNumber sub (ComplexNumber minuend, ComplexNumber subtrahend) {
    return new ComplexNumber(
      minuend.real - subtrahend.real,
      minuend.imag - subtrahend.imag
    );
  }

  public final ComplexNumber sub (ComplexNumber subtrahend) {
    return sub(this, subtrahend);
  }

  public final static ComplexNumber mul (ComplexNumber multiplicand, ComplexNumber multiplier) {
    return new ComplexNumber(
      (multiplicand.real * multiplier.real) - (multiplicand.imag * multiplier.imag),
      (multiplicand.real * multiplier.imag) + (multiplicand.imag * multiplier.real)
    );
  }

  public final ComplexNumber mul (ComplexNumber multiplier) {
    return mul(this, multiplier);
  }

  public final static ComplexNumber div (ComplexNumber dividend, ComplexNumber divisor) {
    if (isReal(dividend, divisor)) {
      return new ComplexNumber(dividend.real / divisor.real);
    }

    return dividend.mul(divisor.rec());
  }

  public final ComplexNumber div (ComplexNumber divisor) {
    return div(this, divisor);
  }

  public final static ComplexNumber pow (ComplexNumber value, ComplexNumber exponent) {
    if (isReal(value, exponent)) {
      return new ComplexNumber(Math.pow(value.real, exponent.real));
    }

    return NaN;
  }

  public final ComplexNumber pow (ComplexNumber exponent) {
    return pow(this, exponent);
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
