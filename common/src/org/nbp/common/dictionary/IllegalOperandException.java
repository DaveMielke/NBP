package org.nbp.common.dictionary;

public class IllegalOperandException extends IllegalResponseException {
  public IllegalOperandException (String operand) {
    super(("illegal operand: " + operand));
  }
}
