package org.nbp.b2g.ui.controls;
import org.nbp.b2g.ui.*;

import org.nbp.common.speech.RateControl;

public class SpeechRateControl extends RateControl {
  @Override
  protected int getResourceForLabel () {
    return R.string.control_label_SpeechRate;
  }

  @Override
  protected int getResourceForGroup () {
    return R.string.control_group_speech;
  }

  @Override
  protected String getPreferenceKey () {
    return "speech-rate";
  }

  @Override
  protected float getFloatDefault () {
    return ApplicationDefaults.SPEECH_RATE;
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
    super();
  }
}
