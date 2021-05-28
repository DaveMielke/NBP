package org.nbp.calculator;

import java.lang.reflect.Method;

public class RealFunction extends ComplexFunction {
  public RealFunction (Method method) {
    super(method);
  }

  public RealFunction (String methodName) {
    super(methodName, Double.TYPE);
  }

  protected double preprocessRealArgument (double argument) {
    return argument;
  }

  protected double postprocessRealResult (double result) {
    return result;
  }

  @Override
  protected final Object preprocessComplexArgument (ComplexNumber argument) {
    {
      Object object = super.preprocessComplexArgument(argument);
      if (!verifyValue(object, ComplexNumber.class)) return null;
      argument = (ComplexNumber)object;
    }

    if (argument.hasImag()) return null;
    return (Double)preprocessRealArgument(argument.real());
  }

  @Override
  protected final ComplexNumber postprocessComplexResult (Object result) {
    if (!verifyValue(result, Double.class)) return null;
    return super.postprocessComplexResult(new ComplexNumber(postprocessRealResult((Double)result)));
  }

  @Override
  public String getArgumentName () {
    return "x";
  }
}
