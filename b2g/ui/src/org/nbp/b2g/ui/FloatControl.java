package org.nbp.b2g.ui;

public abstract class FloatControl extends Control {
  protected abstract float getScale ();
  protected abstract float getExternalValue ();
  protected abstract boolean setExternalValue (float value);

  protected float toInternalValue (float value) {
    return value;
  }

  protected float toExternalValue (float value) {
    return value;
  }

  private boolean adjustControl (int steps) {
    float scale = getScale();
    int oldValue = Math.round(toInternalValue(getExternalValue()) * scale);

    int newValue = oldValue + steps;
    if (!setExternalValue(toExternalValue(newValue / scale))) return false;

    message(getLabel(), newValue);
    return true;
  }

  public boolean up () {
    return adjustControl(1);
  }

  public boolean down () {
    return adjustControl(-1);
  }

  protected FloatControl () {
    super();
  }
}
