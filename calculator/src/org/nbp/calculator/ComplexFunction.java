package org.nbp.calculator;

import java.lang.reflect.Method;

public class ComplexFunction extends Function {
  public ComplexFunction (Method method) {
    super(method);
  }

  public ComplexFunction (String methodName, Class argumentType) {
    super(methodName, argumentType);
  }

  public ComplexFunction (String methodName) {
    this(methodName, ComplexNumber.class);
  }

  protected Object preprocessComplexArgument (ComplexNumber argument) {
    return argument;
  }

  protected ComplexNumber postprocessComplexResult (Object result) {
    if (!verifyValue(result, ComplexNumber.class)) return null;
    return (ComplexNumber)result;
  }

  @Override
  protected final Object preprocessFunctionArgument (Object argument) {
    if (!verifyValue(argument, ComplexNumber.class)) return null;
    return preprocessComplexArgument((ComplexNumber)argument);
  }

  @Override
  protected final Object postprocessFunctionResult (Object result) {
    if (!verifyValue(result)) return null;
    return postprocessComplexResult(result);
  }

  @Override
  public String getArgumentName () {
    return "z";
  }
}
