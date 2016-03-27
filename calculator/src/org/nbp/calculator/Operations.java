package org.nbp.calculator;

public abstract class Operations {
  public abstract Class<? extends ComplexFunction> getFunctionType ();
  public abstract Class<?> getArgumentType ();

  protected Operations () {
  }
}
