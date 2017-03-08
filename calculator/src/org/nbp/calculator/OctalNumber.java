package org.nbp.calculator;

public class OctalNumber extends WholeNumber {
  public final static int RADIX = 0X8;

  public OctalNumber (long number) {
    super(number);
  }

  public OctalNumber (String number) {
    super(number, RADIX);
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new OctalNumber(value);
  }

  @Override
  protected final WholeFormatter newWholeFormatter () {
    return new OctalFormatter();
  }
}
