package org.nbp.calculator;

import java.lang.reflect.Method;

public class ComplexFunction extends Function {
  protected Object preprocessComplexArgument (ComplexNumber argument) {
    return argument;
  }

  protected ComplexNumber postprocessComplexResult (Object result) {
    return (ComplexNumber)result;
  }

  public final ComplexNumber call (ComplexNumber argument) {
    Object methodArgument = preprocessComplexArgument(argument);

    if (methodArgument != null) {
      Object methodResult = callMethod(methodArgument);

      if (methodResult != null) {
        ComplexNumber functionResult = postprocessComplexResult(methodResult);
        if (functionResult != null) return functionResult;
      }
    }

    return ComplexNumber.NaN;
  }

  public ComplexFunction (Method method) {
    super(method);
  }

  @Override
  public String getArgumentName () {
    return "z";
  }
}
