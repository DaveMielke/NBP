package org.nbp.common.dictionary;

public class IllegalCommandOperandException extends DictionaryException {
  private final String operandProblem;

  public IllegalCommandOperandException (String operand, String problem) {
    super(operand);
    operandProblem = problem;
  }

  public final String getProblem () {
    return operandProblem;
  }
}
