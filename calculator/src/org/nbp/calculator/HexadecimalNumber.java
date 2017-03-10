package org.nbp.calculator;

public class HexadecimalNumber extends WholeNumber {
  public final static int RADIX = 0X10;

  public HexadecimalNumber (long number) {
    super(number);
  }

  public HexadecimalNumber () {
    this(0);
  }

  public HexadecimalNumber (String number) {
    super(number, RADIX);
  }

  public final static HexadecimalNumber valueOf (long value) {
    return new HexadecimalNumber(value);
  }

  @Override
  public final String toString () {
    return Long.toHexString(value).toUpperCase();
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new HexadecimalNumber(value);
  }
}
