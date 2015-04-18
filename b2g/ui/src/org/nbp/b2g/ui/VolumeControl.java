package org.nbp.b2g.ui;

public class VolumeControl extends LinearFloatControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.volume_control_label);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-volume";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_VOLUME;
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
