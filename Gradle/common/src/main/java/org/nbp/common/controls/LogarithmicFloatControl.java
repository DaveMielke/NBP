package org.nbp.common.controls;
import org.nbp.common.*;

public abstract class LogarithmicFloatControl extends FloatControl {
  @Override
  protected float toLinearValue (float floatValue) {
    return (float)Math.log10(floatValue);
  }

  @Override
  protected float toFloatValue (float linearValue) {
    return (float)Math.pow(10.0, linearValue);
  }

  @Override
  protected float getLinearScale () {
    return 10.0f / (float)Math.log10(2.0f);
  }

  protected LogarithmicFloatControl () {
    super();
  }
}
