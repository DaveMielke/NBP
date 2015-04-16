package org.nbp.b2g.ui;

public abstract class IntegerControl extends Control {
  protected abstract int getIntegerValue ();
  protected abstract boolean setIntegerValue (int value);

  private boolean adjustValue (int steps) {
    int oldValue = getIntegerValue();
    int newValue = oldValue + steps;
    if (!setIntegerValue(newValue)) return false;
    reportValue();
    return true;
  }

  @Override
  public boolean next () {
    return adjustValue(1);
  }

  @Override
  public boolean previous () {
    return adjustValue(-1);
  }

  @Override
  public String getValue () {
    return Integer.toString(getIntegerValue());
  }

  protected IntegerControl () {
    super();
  }
}
