package org.nbp.calculator;

public class BinaryEvaluator extends WholeEvaluator {
  public BinaryEvaluator (String expression) throws ExpressionException {
    super(expression);
  }

  @Override
  protected final int getRadix () {
    return BinaryNumber.RADIX;
  }
}
