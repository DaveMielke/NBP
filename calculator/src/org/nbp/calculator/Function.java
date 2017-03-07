package org.nbp.calculator;

import java.lang.reflect.Method;

import org.nbp.common.LanguageUtilities;

public abstract class Function {
  private final Method functionMethod;

  protected Function (Method method) {
    functionMethod = method;
  }

  private final Object callMethod (Object argument) {
    return LanguageUtilities.invokeMethod(functionMethod, null, argument);
  }

  public final String getName () {
    return functionMethod.getName();
  }

  public abstract String getArgumentName ();
  public final static char ARGUMENT_PREFIX = '(';
  public final static char ARGUMENT_SUFFIX = ')';

  public final String getCall () {
    return getName() + ARGUMENT_PREFIX + getArgumentName() + ARGUMENT_SUFFIX;
  }

  public final String getSummary () {
    return Operations.getSummary(functionMethod);
  }

  protected final boolean verifyType (Object object, Class type) {
    if (object == null) return false;
    if (!LanguageUtilities.canAssign(type, object.getClass())) return false;
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
