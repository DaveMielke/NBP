package org.nbp.navigator.controls;
import org.nbp.navigator.*;

import org.nbp.common.speech.BalanceControl;

public class SpeechBalanceControl extends BalanceControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechBalance;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-balance";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationDefaults.SPEECH_BALANCE;
  }

  @Override
  public float getFloatValue () {
    return ApplicationSettings.SPEECH_BALANCE;
  }

  @Override
  protected boolean setFloatValue (float value) {
    if (!Announcements.getAnnouncer().setBalance(value)) return false;
    ApplicationSettings.SPEECH_BALANCE = value;
    Announcements.confirmSetting(R.string.control_label_SpeechBalance, getValue());
    return true;
  }

  public SpeechBalanceControl () {
    super();
  }
}
