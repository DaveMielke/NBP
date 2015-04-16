package org.nbp.b2g.ui;

public class BalanceControl extends LinearFloatControl {
  @Override
  protected String getLabel () {
    return "balance";
  }

  @Override
  public String getValue () {
    float value = getFloatValue();
    if (value == 0.0f) return "center";

    StringBuilder sb = new StringBuilder();
    float maximum = SpeechDevice.MAXIMUM_BALANCE;

    if (value < 0.0f) {
      sb.append("left");
      value = -value;
    } else {
      sb.append("right");
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
  protected float getFloatValue () {
    return Devices.getSpeechDevice().getBalance();
  }

  @Override
  protected boolean setFloatValue (float value) {
    return Devices.getSpeechDevice().setBalance(value);
  }

  public BalanceControl () {
    super();
  }
}
