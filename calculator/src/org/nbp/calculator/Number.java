package org.nbp.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Number {
  private final double real;
  private final double imag;

  public Number (double r, double i) {
    real = r;
    imag = i;
  }

  public Number (double r) {
    this(r, 0);
  }

  public final static Number reciprocal (Number number) {
    return new Number(0, 0);
  }

  public final Number reciprocal () {
    return reciprocal(this);
  }

  public final static Number add (Number augend, Number addend) {
    return new Number(
      augend.real + addend.real,
      augend.imag + addend.imag
    );
  }

  public final Number add (Number addend) {
    return add(this, addend);
  }

  public final static Number subtract (Number minuend, Number subtrahend) {
    return new Number(
      minuend.real - subtrahend.real,
      minuend.imag - subtrahend.imag
    );
  }

  public final Number subtract (Number subtrahend) {
    return subtract(this, subtrahend);
  }

  public final static Number multiply (Number multiplicand, Number multiplier) {
    return new Number(
      (multiplicand.real * multiplier.real) - (multiplicand.imag * multiplier.imag),
      (multiplicand.real * multiplier.imag) + (multiplicand.imag * multiplier.real)
    );
  }

  public final Number multiply (Number multiplier) {
    return multiply(this, multiplier);
  }

  public final static Number divide (Number dividend, Number divisor) {
    return dividend.multiply(divisor.reciprocal());
  }

  public final Number divide (Number divisor) {
    return divide(this, divisor);
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
  public final static char ADDITION_SIGN = '+';
  public final static char SUBTRACTION_SIGN = '−';
  public final static char MULTIPLICATION_SIGN = '×';
  public final static char DIVISION_SIGN = '÷';
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

  private final static String toString (double value, boolean imaginary) {
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

  public final static String toString (double r, double i) {
    if (i == 0d) return toString(r, false);
    if (r == 0d) return toString(i, true);

    StringBuilder sb = new StringBuilder();
    sb.append(toString(r, false));
    sb.append(' ');

    if (i < 0d) {
      sb.append(SUBTRACTION_SIGN);
      i = -i;
    } else {
      sb.append(ADDITION_SIGN);
    }

    sb.append(' ');
    sb.append(toString(i, true));
    return sb.toString();
  }

  public final static String toString (double value) {
    return toString(value, 0d);
  }

  public final String toString () {
    return toString(real, imag);
  }
}
