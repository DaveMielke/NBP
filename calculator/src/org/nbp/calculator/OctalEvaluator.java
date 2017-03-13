package org.nbp.calculator;

public class OctalEvaluator extends WholeEvaluator {
  public OctalEvaluator () {
    super();
  }

  @Override
  protected final int getRadix () {
    return OctalNumber.RADIX;
  }
}
