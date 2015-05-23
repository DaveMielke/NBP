package org.nbp.b2g.ui;

public class SpeechVolumeControl extends LinearFloatControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.speechVolume_control_label);
  }

  @Override
  public String getNextLabel () {
    return ApplicationContext.getString(R.string.speechVolume_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return ApplicationContext.getString(R.string.speechVolume_control_previous);
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
    return Devices.speech.get().getVolume();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.speech.get().setVolume(value);
  }

  public SpeechVolumeControl () {
    super(false);
  }
}
