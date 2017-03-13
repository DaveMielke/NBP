package org.nbp.calculator;

public class HexadecimalEvaluator extends WholeEvaluator {
  public HexadecimalEvaluator () {
    super();
  }

  @Override
  protected final int getRadix () {
    return HexadecimalNumber.RADIX;
  }
}
