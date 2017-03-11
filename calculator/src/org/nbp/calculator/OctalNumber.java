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
    String string = toPureString();
    char prefix = '0';
    if (string.charAt(0) != prefix) string = prefix + string;
    return string;
  }

  @Override
  protected final WholeNumber newWholeNumber (long value) {
    return new OctalNumber(value);
  }
}
