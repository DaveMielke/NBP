package org.nbp.calculator;

public abstract class AbstractNumber {
  protected AbstractNumber () {
  }

  public abstract boolean isValid ();
  public abstract String format ();
}
