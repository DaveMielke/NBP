package org.nbp.calculator;

import java.lang.reflect.Method;

import org.nbp.common.LanguageUtilities;

public class Function {
  private final Method method;

  protected Object preprocessArgument (ComplexNumber argument) {
    return argument;
  }

  protected ComplexNumber postprocessResult (Object result) {
    return (ComplexNumber)result;
  }

  public final ComplexNumber call (ComplexNumber argument) {
    Object methodArgument = preprocessArgument(argument);

    if (methodArgument != null) {
      Object methodResult = LanguageUtilities.invokeMethod(method, null, methodArgument);

      if (methodResult != null) {
        ComplexNumber functionResult = postprocessResult(methodResult);
        if (functionResult != null) return functionResult;
      }
    }

    return ComplexNumber.NaN;
  }

  public Function (Method method) {
    this.method = method;
  }
}
