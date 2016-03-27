package org.nbp.calculator;

import java.lang.reflect.Method;

import org.nbp.common.LanguageUtilities;

public abstract class Function {
  public abstract String getArgumentName ();

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

  public final String getCall () {
    return getName() + '(' + getArgumentName() + ')';
  }

  public final String getSummary () {
    return Operations.getSummary(functionMethod);
  }
}
