package org.nbp.calculator;

public class OctalNumber extends WholeNumber {
  public final static int RADIX = 0X8;

  public OctalNumber (long number) {
    super(number);
  }

  public OctalNumber () {
    this(0);
  }

  public OctalNumber (String number) {
    super(number, RADIX);
  }

  public final static OctalNumber valueOf (long value) {
    return new OctalNumber(value);
  }

  @Override
  public final String toString () {
    return Long.toOctalString(value);
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new OctalNumber(value);
  }
}
