package org.nbp.calculator;

import java.lang.reflect.Method;

import org.nbp.common.LanguageUtilities;

public abstract class Function {
  private final Method functionMethod;

  protected Function (Method method) {
    functionMethod = method;
  }

  protected final Object callMethod (Object argument) {
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
}
