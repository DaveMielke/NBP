package org.nbp.b2g.ui;

public class SpeechRateControl extends LogarithmicFloatControl {
  @Override
  public String getLabel () {
    return ApplicationContext.getString(R.string.speechRate_control_label);
  }

  @Override
  public String getNextLabel () {
    return ApplicationContext.getString(R.string.speechRate_control_next);
  }

  @Override
  public String getPreviousLabel () {
    return ApplicationContext.getString(R.string.speechRate_control_previous);
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
  protected float getFloatValue () {
    return ApplicationSettings.SPEECH_RATE;
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.speech.get().setRate(value);
  }

  public SpeechRateControl () {
    super(false);
  }
}
