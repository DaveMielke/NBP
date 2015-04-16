package org.nbp.b2g.ui;

public class RateControl extends LogarithmicFloatControl {
  @Override
  protected String getLabel () {
    return "rate";
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-rate";
  }

  @Override
  protected float getFloatValue () {
    return Devices.getSpeechDevice().getRate();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.getSpeechDevice().setRate(value);
  }

  public RateControl () {
    super();
  }
}
