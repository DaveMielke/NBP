package org.nbp.calculator;

public class HexadecimalFormatter extends WholeFormatter {
  public HexadecimalFormatter () {
    super();
  }

  @Override
  public final String format (long value) {
    return Long.toHexString(value).toUpperCase();
  }
}
