package org.nbp.b2g.ui;

public class RateControl extends LogarithmicFloatControl {
  @Override
  protected String getLabel () {
    return "rate";
  }

  @Override
  protected float getExternalValue () {
    return Devices.getSpeechDevice().getRate();
  }

  @Override
  protected boolean setExternalValue (float value) {
    return Devices.getSpeechDevice().setRate(value);
  }

  public RateControl () {
    super();
  }
}
