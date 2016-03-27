package org.nbp.calculator;

import java.lang.reflect.Method;

public class ComplexFunction extends Function {
  protected Object preprocessArgument (ComplexNumber argument) {
    return argument;
  }

  protected ComplexNumber postprocessResult (Object result) {
    return (ComplexNumber)result;
  }

  public final ComplexNumber call (ComplexNumber argument) {
    Object methodArgument = preprocessArgument(argument);

    if (methodArgument != null) {
      Object methodResult = callFunctionMethod(methodArgument);

      if (methodResult != null) {
        ComplexNumber functionResult = postprocessResult(methodResult);
        if (functionResult != null) return functionResult;
      }
    }

    return ComplexNumber.NaN;
  }

  public ComplexFunction (Method method) {
    super(method);
  }
}
