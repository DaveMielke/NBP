package org.nbp.common.dictionary;

public class IllegalResponseOperandException extends IllegalResponseException {
  private final String operandProblem;

  public IllegalResponseOperandException (String operand, String problem) {
    super(operand);
    operandProblem = problem;
  }

  public final String getProblem () {
    return operandProblem;
  }
}
