package org.nbp.calculator;

public class OctalEvaluator extends WholeEvaluator {
  public OctalEvaluator (String expression) throws ExpressionException {
    super(expression);
  }

  @Override
  protected final int getRadix () {
    return OctalNumber.RADIX;
  }
}
