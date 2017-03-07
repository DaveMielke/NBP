package org.nbp.calculator;

import java.lang.reflect.Method;

public class ComplexFunction extends Function {
  protected Object preprocessComplexArgument (ComplexNumber argument) {
    return argument;
  }

  protected ComplexNumber postprocessComplexResult (Object result) {
    return (ComplexNumber)result;
  }

  @Override
  protected final Object preprocessFunctionArgument (Object argument) {
    if (!verifyType(argument, ComplexNumber.class)) return null;
    return preprocessComplexArgument((ComplexNumber)argument);
  }

  @Override
  protected final Object postprocessFunctionResult (Object result) {
    if (!verifyType(result, ComplexNumber.class)) return null;
    return postprocessComplexResult(result);
  }

  public ComplexFunction (Method method) {
    super(method);
  }

  @Override
  public String getArgumentName () {
    return "z";
  }
}
