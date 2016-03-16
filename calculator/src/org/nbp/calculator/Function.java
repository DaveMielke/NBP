package org.nbp.calculator;

public abstract class Function {
  protected abstract double evaluate (double argument);

  protected double preprocessArgument (double argument) {
    return argument;
  }

  protected double postprocessResult (double result) {
    return result;
  }

  public final double call (double argument) {
    return postprocessResult(evaluate(preprocessArgument(argument)));
  }

  public Function () {
  }
}
