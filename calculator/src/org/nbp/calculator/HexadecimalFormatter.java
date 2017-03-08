package org.nbp.calculator;

public class HexadecimalFormatter extends GenericFormatter {
  public HexadecimalFormatter () {
    super();
  }

  public final String format (long value) {
    return String.format("%X", value);
  }
}
