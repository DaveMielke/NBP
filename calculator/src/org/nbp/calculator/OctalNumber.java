package org.nbp.calculator;

public class OctalNumber extends WholeNumber {
  public final static int RADIX = 0X8;

  public OctalNumber (long number) {
    super(number);
  }

  public OctalNumber () {
    this(0);
  }

  public final static OctalNumber valueOf (String string) {
    return new OctalNumber(toLong(string));
  }

  @Override
  public final String toPureString () {
    return Long.toOctalString(value);
  }

  @Override
  public final String toString () {
    return toPureString();
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new OctalNumber(value);
  }
}
