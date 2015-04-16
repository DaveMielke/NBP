package org.nbp.b2g.ui;

public class PitchControl extends LogarithmicFloatControl {
  @Override
  protected String getLabel () {
    return "pitch";
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-pitch";
  }

  @Override
  protected float getFloatValue () {
    return Devices.getSpeechDevice().getPitch();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.getSpeechDevice().setPitch(value);
  }

  public PitchControl () {
    super();
  }
}
