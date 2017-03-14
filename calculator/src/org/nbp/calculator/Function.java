package org.nbp.calculator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.nbp.common.LanguageUtilities;

public abstract class Function {
  private final Method functionMethod;

  protected Function (Method method) {
    functionMethod = method;
  }

  protected Function (String methodName, Class argumentType) {
    for (Method method : getClass().getDeclaredMethods()) {
      if (!method.getName().equals(methodName)) continue;

      int modifiers = method.getModifiers();
      if ((modifiers & Modifier.PUBLIC) == 0) continue;
      if ((modifiers & Modifier.STATIC) != 0) continue;

      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 1) continue;
      if (parameterTypes[0] != argumentType) continue;

      if (method.getReturnType() != argumentType) continue;
      functionMethod = method;
      return;
    }

    throw new RuntimeException(("function method not found: " + methodName));
  }

  private final Object callMethod (Object argument) {
    return LanguageUtilities.invokeMethod(functionMethod, this, argument);
  }

  public String getName () {
    return functionMethod.getName();
  }

  public abstract String getArgumentName ();
  public final static char ARGUMENT_PREFIX = '(';
  public final static char ARGUMENT_SUFFIX = ')';

  public final String getCall () {
    return getName() + ARGUMENT_PREFIX + getArgumentName() + ARGUMENT_SUFFIX;
  }

  public String getSummary () {
    return Operations.getSummary(functionMethod);
  }

  protected final boolean verifyValue (Object value) {
    if (value == null) return false;
    return true;
  }

  protected final boolean verifyValue (Object value, Class type) {
    if (!verifyValue(value)) return false;
    if (!LanguageUtilities.canAssign(type, value)) return false;
    return true;
  }

  protected abstract Object preprocessFunctionArgument (Object argument);
  protected abstract Object postprocessFunctionResult (Object result);

  public final Object call (Object functionArgument) {
    Object methodArgument = preprocessFunctionArgument(functionArgument);

    if (methodArgument != null) {
      Object methodResult = callMethod(methodArgument);

      if (methodResult != null) {
        Object functionResult = postprocessFunctionResult(methodResult);
        if (functionResult != null) return functionResult;
      }
    }

    return null;
  }
}
