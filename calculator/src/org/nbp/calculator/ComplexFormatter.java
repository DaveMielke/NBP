package org.nbp.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComplexFormatter extends ComplexCommon {
  private final char DECIMAL_SEPARATOR;
  private final char GROUPING_SEPARATOR;
  private final int GROUPING_SIZE;

  private ComplexFormatter () {
    super();

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

  private final static String getMatch (String string, Matcher matcher, int group) {
    int start = matcher.start(group);
    if (start < 0) return "";

    int end = matcher.end(group);
    if (end < 0) return "";

    return string.substring(start, end);
  }

  private final String format (double value, boolean imaginary) {
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

  public final String format (double r, double i) {
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

  public final String format (double value) {
    return format(value, ZERO);
  }

  private final static Object INSTANCE_LOCK = new Object();
  private static ComplexFormatter instance = null;

  public final static ComplexFormatter getInstance () {
    synchronized (INSTANCE_LOCK) {
      if (instance == null) instance = new ComplexFormatter();
      return instance;
    }
  }

  public final static void resetInstance () {
    synchronized (INSTANCE_LOCK) {
      instance = null;
    }
  }
}
