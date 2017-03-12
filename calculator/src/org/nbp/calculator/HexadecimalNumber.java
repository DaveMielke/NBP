package org.nbp.calculator;

public class HexadecimalNumber extends WholeNumber {
  public final static int RADIX = 0X10;

  public HexadecimalNumber (long number) {
    super(number);
  }

  public HexadecimalNumber () {
    this(0);
  }

  public final static HexadecimalNumber valueOf (String string) {
    return new HexadecimalNumber(toLong(string));
  }

  @Override
  public final String toDigits () {
    return Long.toHexString(value).toUpperCase();
  }

  @Override
  public final String toString () {
    return "0X" + toDigits();
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new HexadecimalNumber(value);
  }
}
