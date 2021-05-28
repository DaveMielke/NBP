package org.nbp.calculator;

import java.math.BigInteger;

public abstract class WholeNumber extends AbstractNumber {
  public abstract String toDigits ();
  protected abstract WholeNumber newWholeNumber (long value);

  protected final long value;

  protected WholeNumber (long number) {
    value = number;
  }

  public final long getValue () {
    return value;
  }

  public final static long valueOf (String string, int radix) {
    return new BigInteger(string, radix).longValue();
  }

  protected final static long toLong (String string) {
    int radix = 10;
    boolean negative = false;
    int start = 0;

    if (start < string.length()) {
      switch (string.charAt(start++)) {
        case '-':
          negative = true;
          /* fall through */
        case '+':
          break;

        default:
          start -= 1;
          break;
      }
    }

    if (start < string.length()) {
      if (string.charAt(start) == '0') {
        radix = 8;
        start += 1;

        if (start < string.length()) {
          switch (string.charAt(start++)) {
            case 'x':
            case 'X':
              radix = 16;
              break;

            case 'b':
            case 'B':
              radix = 2;
              break;

            default:
              start -= 1;
              break;
          }
        }
      }
    }

    if (start < string.length()) {
      if (Character.digit(string.charAt(start), radix) < 0) {
        throw new NumberFormatException(("invalid long value: " + string));
      }
    }

    long value = valueOf(string.substring(start), radix);
    if (negative) value = -value;
    return value;
  }

  @Override
  public final String format () {
    return new WholeFormatter().format(this);
  }

  @Override
  public final boolean isValid () {
    return true;
  }

  public final WholeNumber not () {
    return newWholeNumber(~value);
  }

  public final WholeNumber and (WholeNumber number) {
    return newWholeNumber(value & number.getValue());
  }

  public final WholeNumber or (WholeNumber number) {
    return newWholeNumber(value | number.getValue());
  }

  public final WholeNumber xor (WholeNumber number) {
    return newWholeNumber(value ^ number.getValue());
  }

  public final WholeNumber lsl (WholeNumber number) {
    return newWholeNumber(value << number.getValue());
  }

  public final WholeNumber lsr (WholeNumber number) {
    return newWholeNumber(value >>> number.getValue());
  }

  public final WholeNumber asr (WholeNumber number) {
    return newWholeNumber(value >> number.getValue());
  }

  public final WholeNumber neg () {
    return newWholeNumber(-value);
  }

  public final WholeNumber add (WholeNumber number) {
    return newWholeNumber(value + number.getValue());
  }

  public final WholeNumber sub (WholeNumber number) {
    return newWholeNumber(value - number.getValue());
  }

  public final WholeNumber mul (WholeNumber number) {
    return newWholeNumber(value * number.getValue());
  }

  public final WholeNumber div (WholeNumber number) {
    return newWholeNumber(value / number.getValue());
  }

  public final WholeNumber mod (WholeNumber number) {
    return newWholeNumber(value % number.getValue());
  }
}
