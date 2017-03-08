package org.nbp.calculator;

public abstract class WholeFormatter extends GenericFormatter {
  public WholeFormatter () {
    super();
  }

  public abstract String format (long value);
}
