package org.nbp.calculator;

public class BinaryNumber extends WholeNumber {
  public final static int RADIX = 0X2;

  public BinaryNumber (long number) {
    super(number);
  }

  public BinaryNumber () {
    this(0);
  }

  public final static BinaryNumber valueOf (String string) {
    return new BinaryNumber(toLong(string));
  }

  @Override
  public final String toPureString () {
    return Long.toBinaryString(value);
  }

  @Override
  public final String toString () {
    return "0B" + toPureString();
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new BinaryNumber(value);
  }
}
