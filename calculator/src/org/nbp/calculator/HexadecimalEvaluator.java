package org.nbp.calculator;

public class HexadecimalEvaluator extends WholeEvaluator {
  public HexadecimalEvaluator (String expression) throws ExpressionException {
    super(expression);
  }

  @Override
  protected final int getRadix () {
    return HexadecimalNumber.RADIX;
  }
}
