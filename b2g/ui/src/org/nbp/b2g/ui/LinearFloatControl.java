package org.nbp.b2g.ui;

public abstract class LinearFloatControl extends FloatControl {
  @Override
  protected float getScale () {
    return 10.0f;
  }

  protected LinearFloatControl () {
    super();
  }
}
