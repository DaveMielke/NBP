package org.nbp.b2g.ui;

public abstract class LogarithmicFloatControl extends FloatControl {
  @Override
  protected float toNormalizedValue (float value) {
    return (float)Math.log10(value);
  }

  @Override
  protected float toFloatValue (float value) {
    return (float)Math.pow(10.0, value);
  }

  @Override
  protected float getFloatScale () {
    return 30.0f;
  }

  protected LogarithmicFloatControl () {
    super();
  }
}
