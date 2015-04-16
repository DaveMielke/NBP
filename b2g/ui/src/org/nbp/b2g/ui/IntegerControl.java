package org.nbp.b2g.ui;

public abstract class IntegerControl extends Control {
  protected abstract int getIntegerValue ();
  protected abstract boolean setIntegerValue (int value);

  private boolean adjustControl (int steps) {
    int oldValue = getIntegerValue();
    int newValue = oldValue + steps;
    if (!setIntegerValue(newValue)) return false;
    report(newValue);
    return true;
  }

  @Override
  public boolean next () {
    return adjustControl(1);
  }

  @Override
  public boolean previous () {
    return adjustControl(-1);
  }

  protected IntegerControl () {
    super();
  }
}
