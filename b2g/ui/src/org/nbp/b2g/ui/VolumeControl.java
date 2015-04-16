package org.nbp.b2g.ui;

public class VolumeControl extends LinearFloatControl {
  @Override
  protected String getLabel () {
    return "volume";
  }

  @Override
  protected float getExternalValue () {
    return Devices.getSpeechDevice().getVolume();
  }

  @Override
  protected boolean setExternalValue (float value) {
    return Devices.getSpeechDevice().setVolume(value);
  }

  public VolumeControl () {
    super();
  }
}
