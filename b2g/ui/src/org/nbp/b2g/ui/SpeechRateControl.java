package org.nbp.b2g.ui;

public class SpeechRateControl extends LogarithmicFloatControl {
  @Override
  public CharSequence getLabel () {
    return getString(R.string.SpeechRate_control_label);
  }

  @Override
  public CharSequence getNextLabel () {
    return getString(R.string.SpeechRate_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return getString(R.string.SpeechRate_control_previous);
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-rate";
  }

  @Override
  protected float getLinearScale () {
    return super.getLinearScale() / 2.0f;
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_RATE;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_RATE;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setRate(value)) return false;
    ApplicationSettings.SPEECH_RATE = value;
    return true;
  }

  public SpeechRateControl () {
    super(false);
  }
}
