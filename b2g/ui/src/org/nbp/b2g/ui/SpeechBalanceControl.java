package org.nbp.b2g.ui;

public class SpeechBalanceControl extends LinearFloatControl {
  @Override
  public CharSequence getLabel () {
    return ApplicationContext.getString(R.string.SpeechBalance_control_label);
  }

  @Override
  public CharSequence getNextLabel () {
    return ApplicationContext.getString(R.string.SpeechBalance_control_next);
  }

  @Override
  public CharSequence getPreviousLabel () {
    return ApplicationContext.getString(R.string.SpeechBalance_control_previous);
  }

  @Override
  public CharSequence getValue () {
    float value = getFloatValue();
    if (value == 0.0f) return ApplicationContext.getString(R.string.SpeechBalance_control_center);

    StringBuilder sb = new StringBuilder();
    float maximum = SpeechDevice.MAXIMUM_BALANCE;

    if (value < 0.0f) {
      sb.append(ApplicationContext.getString(R.string.SpeechBalance_control_left));
      value = -value;
    } else {
      sb.append(ApplicationContext.getString(R.string.SpeechBalance_control_right));
    }

    sb.append(' ');
    sb.append(Math.round((value * 100.0) / maximum));
    sb.append('%');

    return sb.toString();
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-balance";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationParameters.DEFAULT_SPEECH_BALANCE;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_BALANCE;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Devices.speech.get().setBalance(value)) return false;
    ApplicationSettings.SPEECH_BALANCE = value;
    return true;
  }

  public SpeechBalanceControl () {
    super(false);
  }
}
