package org.nbp.b2g.ui;

public class SpeechPitchControl extends LogarithmicFloatControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.SpeechPitch_control_label);
  }

  @Override
  public CharSequence getNextLabel () {
    return ApplicationContext.getString(R.string.SpeechPitch_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return ApplicationContext.getString(R.string.SpeechPitch_control_previous);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-pitch";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_PITCH;
  }

  @Override
  protected float getFloatValue () {
    return ApplicationSettings.SPEECH_PITCH;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setPitch(value)) return false;
    ApplicationSettings.SPEECH_PITCH = value;
    return true;
  }

  public SpeechPitchControl () {
    super(false);
  }
}
