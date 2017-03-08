package org.nbp.calculator;

public class OctalFormatter extends WholeFormatter {
  public OctalFormatter () {
    super();
  }

  @Override
  public final String format (long value) {
    return Long.toOctalString(value);
  }
}
