package org.nbp.calculator;

public class BinaryFormatter extends WholeFormatter {
  public BinaryFormatter () {
    super();
  }

  @Override
  public final String format (long value) {
    return Long.toBinaryString(value);
  }
}
