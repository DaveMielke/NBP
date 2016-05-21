package org.nbp.b2g.ui;

public abstract class LinearFloatControl extends FloatControl {
  @Override
  protected float getLinearScale () {
    return 10.0f;
  }

  protected LinearFloatControl (ControlGroup group) {
    super(group);
  }
}
