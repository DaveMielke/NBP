package org.nbp.b2g.ui;

public abstract class FloatControl extends IntegerControl {
  protected abstract float getScale ();
  protected abstract float getFloatValue ();
  protected abstract boolean setFloatValue (float value);

  protected float toNormalizedValue (float value) {
    return value;
  }

  protected float toFloatValue (float value) {
    return value;
  }

  @Override
  protected int getIntegerValue () {
    return Math.round(toNormalizedValue(getFloatValue()) * getScale());
  }

  @Override
  protected boolean setIntegerValue (int value) {
    return setFloatValue(toFloatValue((float)value / getScale()));
  }

  protected FloatControl () {
    super();
  }
}
