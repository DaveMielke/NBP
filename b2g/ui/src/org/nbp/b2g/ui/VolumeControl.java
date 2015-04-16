package org.nbp.b2g.ui;

public class VolumeControl extends LinearFloatControl {
  @Override
  protected String getLabel () {
    return "volume";
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-volume";
  }

  @Override
  protected float getFloatValue () {
    return Devices.getSpeechDevice().getVolume();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.getSpeechDevice().setVolume(value);
  }

  public VolumeControl () {
    super();
  }
}
