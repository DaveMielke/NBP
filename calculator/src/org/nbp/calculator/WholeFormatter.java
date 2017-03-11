package org.nbp.calculator;

public class WholeFormatter extends GenericFormatter {
  public WholeFormatter () {
    super();
  }

  public final String format (WholeNumber number) {
    StringBuilder sb = new StringBuilder(number.toPureString());

    {
      int index = sb.length();
      while ((index -= 4) > 0) sb.insert(index, ' ');
    }

    return sb.toString();
  }
}
