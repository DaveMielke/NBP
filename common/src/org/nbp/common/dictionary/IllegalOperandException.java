package org.nbp.common.dictionary;

public class IllegalOperandException extends IllegalResponseException {
  private final String operandProblem;

  public IllegalOperandException (String operand, String problem) {
    super(operand);
    operandProblem = problem;
  }

  public final String getProblem () {
    return operandProblem;
  }
}
