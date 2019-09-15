package org.nbp.common.dictionary;

public class UnclosedQuoteException extends IllegalOperandException {
  public UnclosedQuoteException (String operand) {
    super(operand);
  }
}
