package org.nbp.calculator;

import java.lang.reflect.Method;

public class RealFunction extends Function {
  protected double preprocessArgument (double argument) {
    return argument;
  }

  protected double postprocessResult (double result) {
    return result;
  }

  @Override
  protected Object preprocessArgument (ComplexNumber argument) {
    argument = (ComplexNumber)super.preprocessArgument(argument);
    if (argument == null) return null;
    if (argument.hasImag()) return null;
    return (Double)preprocessArgument(argument.real());
  }

  @Override
  protected ComplexNumber postprocessResult (Object result) {
    if (result == null) return null;
    if (!(result instanceof Double)) return null;
    return super.postprocessResult(new ComplexNumber(postprocessResult((double)(Double)result)));
  }

  public RealFunction (Method method) {
    super(method);
  }
}
