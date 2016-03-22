package org.nbp.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComplexNumber {
  private final double real;
  private final double imag;

  private final static double ZERO = 0d;

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

  private final static char DECIMAL_SEPARATOR;
  private final static char GROUPING_SEPARATOR;
  private final static int GROUPING_SIZE;

  static {
    DecimalFormat format = new DecimalFormat();
    GROUPING_SIZE = format.getGroupingSize();

    DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
    DECIMAL_SEPARATOR = symbols.getDecimalSeparator();
    GROUPING_SEPARATOR = symbols.getGroupingSeparator();
  }

  public final static char IMAGINARY_SIGN = 'i';
  public final static char INFINITY_SIGN = '\u221E';
  public final static char NaN_SIGN = '?';

  public final static char ADDITION_SIGN = '+';
  public final static char SUBTRACTION_SIGN = '\u2212';
  public final static char MULTIPLICATION_SIGN = '\u00D7';
  public final static char DIVISION_SIGN = '\u00F7';
  public final static char EXPONENTIATION_SIGN = '^';

  private final static String DECIMAL_DIGIT = "[0-9]";

  private final static Pattern REAL_PATTERN = Pattern.compile(
    "^"
  + "([-+])?"
  + "0*(" + DECIMAL_DIGIT + "*?)"
  + "(?:\\.(" + DECIMAL_DIGIT + "*?)0*)?"
  + "(?:[Ee]([-+])?0*(" + DECIMAL_DIGIT + "+?))?"
  + "$"
  );

  private static String getMatch (String string, Matcher matcher, int group) {
    int start = matcher.start(group);
    if (start < 0) return "";

    int end = matcher.end(group);
    if (end < 0) return "";

    return string.substring(start, end);
  }

  private final static String format (double value, boolean imaginary) {
    String string = String.format("%.12E", value);
    Matcher matcher = REAL_PATTERN.matcher(string);

    if (matcher.lookingAt()) {
      String sign = getMatch(string, matcher, 1);
      String before = getMatch(string, matcher, 2);
      String after = getMatch(string, matcher, 3);
      String exponentSign = getMatch(string, matcher, 4);
      String exponentValue = getMatch(string, matcher, 5);

      if (!exponentSign.equals("-")) exponentSign = "";
      if (exponentValue.isEmpty()) exponentValue = "0";
      int exponent = Integer.valueOf((exponentSign + exponentValue));

      StringBuilder sb = new StringBuilder();
      sb.append(before);
      exponent += before.length();
      sb.append(after);

      {
        int end = sb.length();

        while (end > 0) {
          if (sb.charAt(end -= 1) != '0') break;
          sb.setLength(end);
        }
      }

      if (sb.length() == 0) {
        sb.append('0');
        exponent = 0;
      } else if ((exponent >= 1) && (exponent <= 12)) {
        if (sb.length() > exponent) {
          sb.insert(exponent, DECIMAL_SEPARATOR);
        } else {
          while (sb.length() < exponent) sb.append('0');
        }

        while ((exponent -= GROUPING_SIZE) > 0) {
          sb.insert(exponent, GROUPING_SEPARATOR);
        }

        exponent = 0;
      } else {
        if (sb.length() > 1) sb.insert(1, DECIMAL_SEPARATOR);
        exponent -= 1;
      }

      if (imaginary) {
        if (sb.toString().equals("1")) sb.setLength(0);
        sb.append(IMAGINARY_SIGN);
      }

      if (exponent != 0) {
        sb.append(MULTIPLICATION_SIGN);
        sb.append(10);
        sb.append(EXPONENTIATION_SIGN);

        if (exponent < 0) {
          sb.append(SUBTRACTION_SIGN);
          exponent = -exponent;
        }

        sb.append(exponent);
      }

      if (sign.equals("-")) sb.insert(0, SUBTRACTION_SIGN);
      string = sb.toString();
    }

    return string;
  }

  public final static String format (double r, double i) {
    if (i == ZERO) return format(r, false);
    if (r == ZERO) return format(i, true);

    StringBuilder sb = new StringBuilder();
    sb.append(format(r, false));
    sb.append(' ');

    if (i < ZERO) {
      sb.append(SUBTRACTION_SIGN);
      i = -i;
    } else {
      sb.append(ADDITION_SIGN);
    }

    sb.append(' ');
    sb.append(format(i, true));
    return sb.toString();
  }

  public final static String format (double value) {
    return format(value, ZERO);
  }

  public final String format () {
    return format(real, imag);
  }

  public final String toString() {
    return (Double.toString(real) + '_' + Double.toString(imag));
  }

  public final static ComplexNumber valueOf (String string) {
    int delimiter = string.indexOf('_');
    if (delimiter < 0) return new ComplexNumber(Double.valueOf(string));

    return new ComplexNumber(
      Double.valueOf(string.substring(0, delimiter)),
      Double.valueOf(string.substring(delimiter+1))
    );
  }
}
