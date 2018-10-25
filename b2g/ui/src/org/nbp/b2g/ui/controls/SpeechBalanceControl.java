package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.speech.BalanceControl;
import org.nbp.common.speech.SpeechParameters;

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
    if (!SpeechParameters.verifyBalance(value)) return false;

    if (Devices.speech.isInstantiated()) {
      Devices.speech.get().setBalance(value);
    }

    ApplicationSettings.SPEECH_BALANCE = value;
    return true;
  }

  public SpeechBalanceControl () {
    super();
  }
}
