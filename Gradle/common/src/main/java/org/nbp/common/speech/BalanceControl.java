package org.nbp.common.speech;
import org.nbp.common.*;

import org.nbp.common.controls.LinearFloatControl;

public abstract class BalanceControl extends LinearFloatControl {
  @Override
  protected int getResourceForNext () {
    return R.string.control_speech_balance_next;
  }

  @Override
  protected int getResourceForPrevious () {
    return R.string.control_speech_balance_previous;
  }

  @Override
  public final CharSequence getValue () {
    float value = getFloatValue();
    if (value == 0.0f) return getString(R.string.control_speech_balance_center);

    StringBuilder sb = new StringBuilder();
    float maximum = Math.abs(SpeechParameters.BALANCE_RIGHT);

    if (value < 0.0f) {
      sb.append(getString(R.string.control_speech_balance_left));
      value = -value;
    } else {
      sb.append(getString(R.string.control_speech_balance_right));
    }

    sb.append(' ');
    sb.append(Math.round((value * 100.0) / maximum));
    sb.append('%');

    return sb.toString();
  }

  public BalanceControl () {
    super();
  }
}
