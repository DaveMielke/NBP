package org.nbp.calculator;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@interface Operation {
  String summary();
}

public abstract class Operations {
  public abstract Class<? extends ComplexFunction> getFunctionType ();
  public abstract Class<?> getArgumentType ();

  protected Operations () {
  }

  public final static String getSummary (Method method) {
    Annotation annotation = method.getAnnotation(Operation.class);
    if (annotation == null) return null;
    return ((Operation)annotation).summary();
  }
}
