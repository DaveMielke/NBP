package org.nbp.calculator;

public class BinaryNumber extends WholeNumber {
  public final static int RADIX = 0X2;

  public BinaryNumber (long number) {
    super(number);
  }

  public BinaryNumber () {
    this(0);
  }

  public BinaryNumber (String number) {
    super(number, RADIX);
  }

  public final static BinaryNumber valueOf (long value) {
    return new BinaryNumber(value);
  }

  @Override
  public final String toString () {
    return Long.toBinaryString(value);
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new BinaryNumber(value);
  }
}
