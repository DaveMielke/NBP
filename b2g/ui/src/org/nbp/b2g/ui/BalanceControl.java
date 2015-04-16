package org.nbp.b2g.ui;

public class BalanceControl extends LinearFloatControl {
  @Override
  protected String getLabel () {
    return "balance";
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-balance";
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
