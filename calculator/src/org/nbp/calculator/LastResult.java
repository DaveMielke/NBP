package org.nbp.calculator;

public class LastResult extends PredefinedVariable {
  private LastResult () {
    super(
      Character.toString(Variables.PREDEFINED_PREFIX),
      "result of most recent expression"
    );
  }

  private AbstractNumber currentValue = null;

  @Override
  protected final AbstractNumber getValue () {
    return currentValue;
  }

  public final void setValue (AbstractNumber value) {
    currentValue = value;
  }

  private final static Object LOCK = new Object();
  private static LastResult variable = null;

  public final static LastResult singleton () {
    synchronized (LOCK) {
      if (variable == null) variable = new LastResult();
    }

    return variable;
  }
}
