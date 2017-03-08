package org.nbp.calculator;

public class HexadecimalNumber extends GenericNumber {
  private final long value;

  public HexadecimalNumber (long v) {
    value = v;
  }

  @Override
  public final boolean isValid () {
    return true;
  }

  @Override
  public final String format () {
    return new HexadecimalFormatter().format(value);
  }
}
