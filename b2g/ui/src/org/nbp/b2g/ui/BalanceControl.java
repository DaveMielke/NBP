package org.nbp.b2g.ui;

public class BalanceControl extends LinearFloatControl {
  @Override
  protected String getLabel () {
    return "balance";
  }

  @Override
  protected float getExternalValue () {
    return Devices.getSpeechDevice().getBalance();
  }

  @Override
  protected boolean setExternalValue (float value) {
    return Devices.getSpeechDevice().setBalance(value);
  }

  public BalanceControl () {
    super();
  }
}
