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

  public final String getSummary () {
    return Operations.getSummary(functionMethod);
  }
}
