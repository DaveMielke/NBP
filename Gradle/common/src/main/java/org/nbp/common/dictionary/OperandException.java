package org.nbp.common.dictionary;

public class OperandException extends DictionaryException {
  private static String makeMessage (String problem, String data) {
    if (data != null) problem += ": " + data;
    return problem;
  }

  public OperandException (String problem, String data) {
    super(makeMessage(problem, data));
  }

  public OperandException (String problem) {
    this(problem, null);
  }
}
