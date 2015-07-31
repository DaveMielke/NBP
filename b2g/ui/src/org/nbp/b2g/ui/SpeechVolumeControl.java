package org.nbp.b2g.ui;

public class SpeechVolumeControl extends LinearFloatControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.SpeechVolume_control_label);
  }

  @Override
  public String getNextLabel () {
    return ApplicationContext.getString(R.string.SpeechVolume_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return ApplicationContext.getString(R.string.SpeechVolume_control_previous);
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
    return ApplicationSettings.SPEECH_VOLUME;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setVolume(value)) return false;
    ApplicationSettings.SPEECH_VOLUME = value;
    return true;
  }

  public SpeechVolumeControl () {
    super(false);
  }
}
