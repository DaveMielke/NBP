package org.nbp.calculator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ComplexFormatter extends GenericFormatter {
  public ComplexFormatter () {
    super();
  }

  private boolean groupingSeparatorEnabled = false;
  private int minimumFixedDigits = 1;
  private int maximumFixedDigits = 0;
  private int significantDigits = 10;
  private int decimalGrouping = 1;

  public final boolean getGroupingSeparatorEnabled () {
    return groupingSeparatorEnabled;
  }

  public final ComplexFormatter setGroupingSeparatorEnabled (boolean enabled) {
    groupingSeparatorEnabled = enabled;
    return this;
  }

  public final int getMinimumFixedDigits () {
    return minimumFixedDigits;
  }

  public final ComplexFormatter setMinimumFixedDigits (int digits) {
    minimumFixedDigits = digits;
    return this;
  }

  public final int getMaximumFixedDigits () {
    return maximumFixedDigits;
  }

  public final ComplexFormatter setMaximumFixedDigits (int digits) {
    maximumFixedDigits = digits;
    return this;
  }

  public final int getSignificantDigits () {
    return significantDigits;
  }

  public final ComplexFormatter setSignificantDigits (int digits) {
    significantDigits = digits;
    return this;
  }

  public final int getDecimalGrouping () {
    return decimalGrouping;
  }

  public final ComplexFormatter setDecimalGrouping (int digits) {
    decimalGrouping = digits;
    return this;
  }

  public final static char IMAGINARY_SIGN = 'i';
  public final static char INFINITY_SIGN = '\u221E';
  public final static char NaN_SIGN = '?';

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
    char decimalSeparator;
    char groupingSeparator;
    int groupingSize;

    synchronized (LocaleData.LOCK) {
      decimalSeparator = LocaleData.getDecimalSeparator();
      groupingSeparator = LocaleData.getGroupingSeparator();
      groupingSize = LocaleData.getGroupingSize();
    }

    String format = "%." + (significantDigits - 1) + "E";
    String string = String.format(format, value);
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
          if (sb.charAt(end -= 1) != ZERO) break;
          sb.setLength(end);
        }
      }

      if (sb.length() == 0) {
        sb.append(ZERO);
        exponent = 0;
      } else if ((exponent >= getMinimumFixedDigits()) &&
                 (exponent <= getMaximumFixedDigits())) {
        if (exponent > 0) {
          if (sb.length() > exponent) {
            sb.insert(exponent, decimalSeparator);
          } else {
            while (sb.length() < exponent) sb.append(ZERO);
          }

          if (getGroupingSeparatorEnabled()) {
            while ((exponent -= groupingSize) > 0) {
              sb.insert(exponent, groupingSeparator);
            }
          }

          exponent = 0;
        } else {
          while (exponent < 0) {
            sb.insert(0, ZERO);
            exponent += 1;
          }

          sb.insert(0, decimalSeparator);
          sb.insert(0, ZERO);
        }
      } else {
        int grouping = Math.max(1, getDecimalGrouping());

        int position = (exponent - 1) % grouping;
        if (position < 0) position += grouping;

        position += 1;
        exponent -= position;

        while (sb.length() < position) sb.append(ZERO);
        if (position < sb.length()) sb.insert(position, decimalSeparator);
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

  public final String format (double real, double imag) {
    if (imag == 0d) return format(real, false);
    if (real == 0d) return format(imag, true);

    StringBuilder sb = new StringBuilder();
    sb.append(format(real, false));
    sb.append(' ');

    if (imag < 0d) {
      sb.append(SUBTRACTION_SIGN);
      imag = -imag;
    } else {
      sb.append(ADDITION_SIGN);
    }

    sb.append(' ');
    sb.append(format(imag, true));
    return sb.toString();
  }

  public final String format (double value) {
    return format(value, 0d);
  }
}
