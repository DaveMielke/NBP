package org.nbp.common.controls;
import org.nbp.common.*;

public abstract class LinearFloatControl extends FloatControl {
  @Override
  protected float getLinearScale () {
    return 10.0f;
  }

  protected LinearFloatControl () {
    super();
  }
}
