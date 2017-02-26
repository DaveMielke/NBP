package org.nbp.calculator;

public class NoExpressionException extends ExpressionException {
  public NoExpressionException (int location) {
    super(R.string.error_no_expression, location);
  }
}
