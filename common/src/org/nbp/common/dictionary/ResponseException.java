package org.nbp.common.dictionary;

public class ResponseException extends DictionaryException {
  private final String operandProblem;

  public ResponseException (String operand, String problem) {
    super(operand);
    operandProblem = problem;
  }

  public final String getProblem () {
    return operandProblem;
  }
}
