package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.LinearFloatControl;

public class SpeechBalanceControl extends LinearFloatControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechBalance;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected int getResourceForNext () {
    return R.string.control_next_SpeechBalance;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_previous_SpeechBalance;
  }

  @Override
  public CharSequence getValue () {
    float value = getFloatValue();
    if (value == 0.0f) return getString(R.string.control_value_SpeechBalance_center);

    StringBuilder sb = new StringBuilder();
    float maximum = SpeechDevice.MAXIMUM_BALANCE;

    if (value < 0.0f) {
      sb.append(getString(R.string.control_value_SpeechBalance_left));
      value = -value;
    } else {
      sb.append(getString(R.string.control_value_SpeechBalance_right));
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
    return ApplicationDefaults.SPEECH_BALANCE;
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
    super();
  }
}
