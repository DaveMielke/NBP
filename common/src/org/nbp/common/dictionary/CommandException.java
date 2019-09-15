package org.nbp.common.dictionary;

public class CommandException extends DictionaryException {
  private final String operandProblem;

  public CommandException (String operand, String problem) {
    super(operand);
    operandProblem = problem;
  }

  public final String getProblem () {
    return operandProblem;
  }
}
