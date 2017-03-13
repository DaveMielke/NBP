package org.nbp.calculator;

public class BinaryEvaluator extends WholeEvaluator {
  public BinaryEvaluator () {
    super();
  }

  @Override
  protected final int getRadix () {
    return BinaryNumber.RADIX;
  }
}
