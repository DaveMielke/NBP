package org.nbp.calculator;

import java.lang.reflect.Method;

import org.nbp.common.LanguageUtilities;

public class Function {
  private final Method method;

  private final double evaluate (double argument) {
    Object result = LanguageUtilities.invokeMethod(method, null, argument);
    if (result != null) return (Double)result;
    return Double.NaN;
  }

  protected double preprocessArgument (double argument) {
    return argument;
  }

  protected double postprocessResult (double result) {
    return result;
  }

  public final double call (double argument) {
    return postprocessResult(evaluate(preprocessArgument(argument)));
  }

  public Function (Method method) {
    this.method = method;
  }
}
