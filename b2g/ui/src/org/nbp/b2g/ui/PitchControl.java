package org.nbp.b2g.ui;

public class PitchControl extends LogarithmicFloatControl {
  @Override
  protected String getLabel () {
    return "pitch";
  }

  @Override
  protected float getExternalValue () {
    return Devices.getSpeechDevice().getPitch();
  }

  @Override
  protected boolean setExternalValue (float value) {
    return Devices.getSpeechDevice().setPitch(value);
  }

  public PitchControl () {
    super();
  }
}
