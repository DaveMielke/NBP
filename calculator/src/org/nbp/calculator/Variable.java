package org.nbp.calculator;

public abstract class Variable {
  private final String variableName;

  protected Variable (String name) {
    variableName = name;
  }

  public final String getName () {
    return variableName;
  }

  public String getDescription () {
    return null;
  }

  protected abstract GenericNumber getValue ();
}
