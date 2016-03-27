package org.nbp.calculator;

import java.lang.reflect.Method;

public class RealFunction extends ComplexFunction {
  protected double preprocessRealArgument (double argument) {
    return argument;
  }

  protected double postprocessRealResult (double result) {
    return result;
  }

  @Override
  protected Object preprocessComplexArgument (ComplexNumber argument) {
    argument = (ComplexNumber)super.preprocessComplexArgument(argument);
    if (argument == null) return null;
    if (argument.hasImag()) return null;
    return (Double)preprocessRealArgument(argument.real());
  }

  @Override
  protected ComplexNumber postprocessComplexResult (Object result) {
    if (result == null) return null;
    if (!(result instanceof Double)) return null;
    return super.postprocessComplexResult(new ComplexNumber(postprocessRealResult((Double)result)));
  }

  public RealFunction (Method method) {
    super(method);
  }

  @Override
  public String getArgumentName () {
    return "x";
  }
}
