package org.nbp.calculator;

public abstract class GenericNumber {
  public abstract boolean isValid ();
  public abstract String format ();

  protected GenericNumber () {
  }
}
