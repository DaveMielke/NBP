package org.nbp.calculator;

public class WholeFormatter extends Formatter {
  public WholeFormatter () {
    super();
  }

  public final String format (WholeNumber number) {
    StringBuilder sb = new StringBuilder(number.toDigits());

    {
      int index = sb.length();
      while ((index -= 4) > 0) sb.insert(index, ' ');
    }

    return sb.toString();
  }
}
