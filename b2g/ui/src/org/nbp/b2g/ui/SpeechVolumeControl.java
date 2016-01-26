package org.nbp.b2g.ui;

public class SpeechVolumeControl extends LinearFloatControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.SpeechVolume_control_label);
  }

  @Override
  public CharSequence getNextLabel () {
    return getString(R.string.SpeechVolume_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getString(R.string.SpeechVolume_control_previous);
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
  public float getFloatValue () {
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
