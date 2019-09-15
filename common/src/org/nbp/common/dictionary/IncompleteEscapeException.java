package org.nbp.common.dictionary;

public class IncompleteEscapeException extends IllegalOperandException {
  public IncompleteEscapeException (String operand) {
    super(operand);
  }
}
