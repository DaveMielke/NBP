package org.nbp.calculator;

public class HexadecimalNumber extends WholeNumber {
  public final static int RADIX = 0X10;

  public HexadecimalNumber (long number) {
    super(number);
  }

  public HexadecimalNumber (String number) {
    super(number, RADIX);
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new HexadecimalNumber(value);
  }

  @Override
  protected final WholeFormatter newWholeFormatter () {
    return new HexadecimalFormatter();
  }
}
